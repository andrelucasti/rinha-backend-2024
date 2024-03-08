package io.andrelucas
package business.statement

import business.client.ClientRepository
import business.transaction.TransactionRepository

trait StatementRepository {
  def findByClientId(clientId: Long): Statement
}

case class InMemoryStatementRepository(transactionRepository: TransactionRepository,
                                       clientRepository: ClientRepository) extends StatementRepository:
  override def findByClientId(clientId: Long): Statement = {
    val client = clientRepository.findById(clientId).get
    Statement(client.balance, List.empty)
  }