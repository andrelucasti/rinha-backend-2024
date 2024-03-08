package io.andrelucas
package business.statement

import business.client.ClientRepository
import business.transaction.TransactionRepository

import scala.util.Try

trait StatementRepository {
  def findByClientId(clientId: Long): Try[Statement]
}

case class InMemoryStatementRepository(transactionRepository: TransactionRepository,
                                       clientRepository: ClientRepository) extends StatementRepository:
  override def findByClientId(clientId: Long): Try[Statement] = 
    Try {
      val client = clientRepository.findById(clientId).get
      Statement(client.balance, List.empty)
    }
  