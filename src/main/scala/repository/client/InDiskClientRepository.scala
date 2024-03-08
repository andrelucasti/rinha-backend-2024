package io.andrelucas
package repository.client

import business.client.{Balance, Client, ClientRepository}

import io.andrelucas.business.transaction.TransactionType
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api.*

import java.time.LocalDateTime
import java.util.concurrent.Executors
import scala.concurrent.duration.*
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

case class InDiskClientRepository(db: Database) extends ClientRepository {
  given ExecutionContext = ExecutionContext.fromExecutor(Executors.newVirtualThreadPerTaskExecutor())
  override def save(client: Client): Unit = ???

  override def findById(clientId: Long): Option[Client] =
    val action = clientTable
      .filter(_.id === clientId).result
      .transactionally

    val future = db.run(action)

    val option = Await.result(future, 2.second)

    option.headOption.map(
      table => ClientEntity(
        table.id,
        table.name,
        table.limit,
        table.balance,
        table.version,
        table.updatedAt
      ).toClient
    )

  override def updateBalance(client: Client, balance: Balance): Unit =

    // Pessimistic lock
    //updateWithPessimisticLock(client, balance)

    // Optimistic lock
    updateWithOptimisticLock(client, balance)


    // no lock
    //updateNoLock(client, balance)


  private def updateNoLock(client: Client, balance: Balance): Unit = {
    val action = clientTable
      .filter(_.id === client.id)
      .map(client => (client.balance, client.updatedAt))
      .update(balance.total, LocalDateTime.now())
      .transactionally

    db.run(action).onComplete{
      case Success(result) =>
        // Check the result to see if the update was successful
        if (result == 1) {
          println("Update successful")
        } else {
          println("Update failed - client not found")
        }

      case Failure(exception) => exception.printStackTrace()
    }
  }

  private def updateWithPessimisticLock(client: Client, balance: Balance): Unit = {

    val action = DBIO.seq(
      sqlu"BEGIN TRANSACTION",
      sqlu" SELECT * FROM clientes WHERE id = ${client.id} FOR UPDATE",

      sql"UPDATE clientes SET balance = ${balance.total}, updated_at = now() WHERE id = ${client.id}".asUpdate,

      sqlu"COMMIT"
    )

    db.run(
      action
    )
  }

  private def updateWithOptimisticLock(client: Client, balance: Balance): Unit = {
    val action = (for {
      clientForUpdate <- clientTable.filter(_.id === client.id).result.head
      update <- clientTable
        .filter(f => f.id === client.id && f.version === clientForUpdate.version)
        .map(c => (c.balance, c.version, c.updatedAt))
        .update(balance.total, clientForUpdate.version + 1, LocalDateTime.now())
    } yield update).transactionally

    db.run(action).onComplete{
      case Success(result) =>
        // Check the result to see if the update was successful
        if (result == 1) {
          println("Update with optimistic lock successful")
        } else {
          println("Update failed - client not found or version mismatch")
        }

      case Failure(exception) => exception.printStackTrace()
    }
  }
}
