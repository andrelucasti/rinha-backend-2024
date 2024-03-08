package io.andrelucas
package repository.transaction

import business.transaction.{Transaction, TransactionRepository}

import io.andrelucas.business.client.{Balance, Limit}
import io.andrelucas.business.statement.Statement
import io.andrelucas.repository.client.{ClientEntity, clientTable}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api.*

import java.time.LocalDateTime
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.util.{Failure, Success}

case class InDiskTransactionRepository(db: Database) extends TransactionRepository {

  override def createTransaction(transaction: Transaction): Balance = {
    val transactionEntity = TransactionEntity.fromTransaction(transaction)
    
    val clientAction = ( for {
      c <- clientTable.filter(_.id === transaction.clientId)
        .forUpdate
        .result
        .head
        .map(c=> ClientEntity(c.id, c.name, c.limit, c.balance, c.version, c.updatedAt).toClient)
      
      newBalance = transaction.transactionType.newBalance(c)
      
      _ <- clientTable.filter(_.id === transaction.clientId)
        .map(client => (client.balance, client.updatedAt))
        .update(newBalance.total, LocalDateTime.now())
      
      _ <- transactionTable
        .map(t => (t.value, t.transactionType, t.description, t.clientId)) +=
        (transactionEntity.value, transactionEntity.transactionType, transactionEntity.description, transactionEntity.clientId)
      
    } yield (newBalance))

    Await.result(db.run(clientAction.transactionally), 5.second)
  }

  override def save(transaction: Transaction): Unit = {
    val transactionEntity = TransactionEntity.fromTransaction(transaction)

    val insert = DBIO.seq(
      transactionTable
        .map(t => (t.value, t.transactionType, t.description, t.clientId)) +=
        (transactionEntity.value, transactionEntity.transactionType, transactionEntity.description, transactionEntity.clientId)
    ).transactionally

    db.run(insert).onComplete{
      case Success(value) => println(s"the transaction was saved by client ${transaction.clientId} with value ${transaction.value} and type ${transaction.transactionType.prefix()}")
      case Failure(exception) => exception.printStackTrace()
    }
  }

  override def findByClientId(clientId: Long): Statement = {
    
    val statementAction = (for {
      balance <- clientTable.filter(_.id === clientId)
        .forUpdate
        .result
        .head
        .map(c=> Balance(c.balance, Limit(c.limit)))
      
      transactionsTableList <- transactionTable.filter(_.clientId === clientId)
        .sortBy(_.updatedAt.desc)
        .take(10)
        .result
      
      transactions = transactionsTableList
        .map(t => TransactionEntity(t.id, t.value, t.transactionType, t.description, t.clientId, t.updatedAt).toTransaction)
        .toList
      
    } yield Statement(balance, transactions, LocalDateTime.now()))
    

    val future = db.run(statementAction.transactionally)
    
    Await.result(future, 5.second)
  }
}
