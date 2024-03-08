package io.andrelucas
package business.transaction

import io.andrelucas.business.client.{Balance, ClientRepository, Limit}
import io.andrelucas.business.statement.Statement

import scala.collection.mutable

trait TransactionRepository {
  def save(transaction: Transaction):Unit
  def findByClientId(clientId: Long): Statement
  def createTransaction(transaction: Transaction): Balance
}

case class InMemoryTransactionRepository(clientRepository: ClientRepository) extends TransactionRepository:
  private val data: scala.collection.mutable.Buffer[Transaction] = mutable.Buffer.empty
  override def save(transaction: Transaction): Unit =
    data += transaction
  
  override def findByClientId(clientId: Long): Statement = ???

  override def createTransaction(transaction: Transaction): Balance =
    data += transaction
    
    val client = clientRepository.findById(transaction.clientId).get
    val balance = transaction.transactionType.newBalance(client)
    clientRepository.updateBalance(client, balance);

    balance
