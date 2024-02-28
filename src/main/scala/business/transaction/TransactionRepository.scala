package io.andrelucas
package business.transaction

import scala.collection.mutable

trait TransactionRepository {
  def save(transaction: Transaction):Unit
  def findByClientId(clientId: Long): List[Transaction]
}

case class InMemoryTransactionRepository() extends TransactionRepository:
  private val data: scala.collection.mutable.Buffer[Transaction] = mutable.Buffer.empty
  override def save(transaction: Transaction): Unit =
    data += transaction
  
  override def findByClientId(clientId: Long): List[Transaction] =
    data.filter(_.clientId == clientId).toList
    
