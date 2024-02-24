package io.andrelucas
package statement

import clients.ClientRepository
import transaction.{Transaction, TransactionRepository}

import scala.collection.mutable

trait StatementRepository {
  def save(transaction: Transaction):Unit
  def findByClientId(clientId: Long): Statement
}

case class InMemoryStatementRepository(clientRepository: ClientRepository) extends StatementRepository:

  private val data: scala.collection.mutable.Buffer[Transaction] = mutable.Buffer.empty

  override def save(transaction: Transaction): Unit = {
    data += transaction
  }

  override def findByClientId(clientId: Long): Statement = {
    val client = clientRepository.findById(clientId).get

    val transactions = data.filter(_.clientId == clientId)
      .sorted(Ordering.by(_.date)).reverse
      .toList

    Statement(client.balance, transactions)
  }