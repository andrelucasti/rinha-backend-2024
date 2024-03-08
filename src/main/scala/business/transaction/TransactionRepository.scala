package io.andrelucas
package business.transaction

import business.client.{Balance, ClientRepository}
import business.statement.Statement

import scala.collection.mutable
import scala.util.Try

trait TransactionRepository {
  def findByClientId(clientId: Long): Try[Statement]
  def createTransaction(transaction: Transaction): Balance
}

case class InMemoryTransactionRepository(clientRepository: ClientRepository) extends TransactionRepository:
  private val data: scala.collection.mutable.Buffer[Transaction] = mutable.Buffer.empty
  override def findByClientId(clientId: Long): Try[Statement] = ???

  override def createTransaction(transaction: Transaction): Balance =
    data += transaction

    val client = clientRepository.findById(transaction.clientId).get
    transaction.transactionType.newBalance(client)