package io.andrelucas
package repository.statement

import business.client.ClientRepository
import business.statement.{Statement, StatementRepository}
import business.transaction.TransactionRepository

case class InDiskStatementRepository(transactionRepository: TransactionRepository,
                                     clientRepository: ClientRepository) extends StatementRepository {

  override def findByClientId(clientId: Long): Statement = 
    transactionRepository.findByClientId(clientId)
}
