package io.andrelucas
package repository.statement

import business.client.ClientRepository
import business.statement.{Statement, StatementRepository}
import business.transaction.TransactionRepository

import scala.util.Try

case class InDiskStatementRepository(transactionRepository: TransactionRepository,
                                     clientRepository: ClientRepository) extends StatementRepository {

  override def findByClientId(clientId: Long): Try[Statement] =
    transactionRepository.findByClientId(clientId)
}
