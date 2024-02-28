package io.andrelucas
package repository.statement

import business.client.ClientRepository
import business.statement.{Statement, StatementRepository}
import business.transaction.TransactionRepository

case class InDiskStatementRepository(transactionRepository: TransactionRepository,
                                     clientRepository: ClientRepository) extends StatementRepository {

  override def findByClientId(clientId: Long): Statement =
    val client = clientRepository.findById(clientId).get

    val transactions = transactionRepository.findByClientId(clientId)
    .sorted(Ordering.by(_.date)).reverse

    Statement(client.balance, transactions)
}
