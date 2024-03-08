package io.andrelucas

import business.client.ClientRepository
import business.statement.{StatementRepository, StatementService}
import business.transaction.{TransactionRepository, TransactionService}
import repository.client.InDiskClientRepository
import repository.statement.InDiskStatementRepository
import repository.transaction.InDiskTransactionRepository

import slick.jdbc.JdbcBackend.Database

object App {

  @main
  def main(): Unit = {
    val db = Database.forConfig("rinhadb")

    val clientRepository: ClientRepository = InDiskClientRepository(db)
    val transactionRepository: TransactionRepository = InDiskTransactionRepository(db)
    
    val statementRepository: StatementRepository = InDiskStatementRepository(transactionRepository, clientRepository)

    val statementService = StatementService(statementRepository)
    val transactionService: TransactionService = TransactionService(transactionRepository, clientRepository)

    AppConfiguration(clientRepository, transactionService, statementService)
      .start(8080)
  }
}
