package io.andrelucas
package repository.transaction

import business.transaction.{Transaction, TransactionRepository}

import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api.*

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.util.{Failure, Success}

case class InDiskTransactionRepository(db: Database) extends TransactionRepository {

  override def save(transaction: Transaction): Unit = {
    val transactionEntity = TransactionEntity.fromTransaction(transaction)

    val insert = DBIO.seq(
      transactionTable
        .map(t => (t.value, t.transactionType, t.description, t.clientId)) +=
        (transactionEntity.value, transactionEntity.transactionType, transactionEntity.description, transactionEntity.clientId)
    ).transactionally

    db.run(insert).onComplete{
      case Success(value) => println(s"the transaction was saved by client ${transaction.clientId} with value ${transaction.value} and type ${transaction.transactionType}")
      case Failure(exception) => exception.printStackTrace()
    }
  }

  override def findByClientId(clientId: Long): List[Transaction] = {
    val action = transactionTable.filter(_.clientId === clientId)
      .result
      .transactionally

    val future = db.run(action)
    val option = Await.result(future, 2.second)

    option.map(
      table => TransactionEntity(table.id, table.value, table.transactionType, table.description, table.clientId, table.updatedAt).toTransaction
    ).toList
  }
}
