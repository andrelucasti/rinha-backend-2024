package io.andrelucas
package repository.client

import business.client.{Balance, Client, ClientRepository}

import scala.concurrent.duration.*
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api.*

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

case class InDiskClientRepository(db: Database) extends ClientRepository {
  override def save(client: Client): Unit = ???

  override def findById(clientId: Long): Option[Client] =
    val action = clientTable
      .filter(_.id === clientId).result

    val future = db.run(action)

    val option = Await.result(future, 2.second)

    option.headOption.map(
      table => ClientEntity(table.id, table.name, table.limit, table.balance, table.updatedAt).toClient
    )


  override def updateBalance(client: Client, balance: Balance): Unit =
    val action = clientTable
      .filter(_.id === client.id)
      .map(client => (client.balance, client.updatedAt))
      .update(balance.total, LocalDateTime.now())

    db.run(action).onComplete{
      case Success(value) => println("uhuuuuu")
      case Failure(exception) => exception.printStackTrace()
    }
}
