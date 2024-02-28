package io.andrelucas

import business.client.{ClientRepository, InMemoryClientRepository}
import business.statement.{InMemoryStatementRepository, StatementRepository, StatementService}
import business.transaction.{TransactionRepository, TransactionService}

import io.andrelucas.repository.client.InDiskClientRepository
import io.andrelucas.repository.transaction.InDiskTransactionRepository
import slick.jdbc.JdbcBackend.Database

object App {

  @main
  def main(): Unit = {
    val db = Database.forConfig("rinhadb")
    val clientRepository: ClientRepository = InDiskClientRepository(db)
    val transactionRepository: TransactionRepository = InDiskTransactionRepository(db)
    
    val statementRepository: StatementRepository = InMemoryStatementRepository(transactionRepository, clientRepository)

    val statementService = StatementService(statementRepository)
    val transactionService: TransactionService = TransactionService(transactionRepository, clientRepository)

    AppConfiguration(clientRepository, transactionService, statementService)
      .start(7070)
  }
}
