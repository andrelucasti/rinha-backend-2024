package io.andrelucas

import business.client.{ClientRepository, InMemoryClientRepository}
import business.statement.{InMemoryStatementRepository, StatementRepository, StatementService}
import business.transaction.{TransactionRepository, TransactionService}

import io.andrelucas.repository.client.InDiskClientRepository
import io.andrelucas.repository.statement.InDiskStatementRepository
import io.andrelucas.repository.transaction.InDiskTransactionRepository
import slick.jdbc.JdbcBackend.Database
import slick.util.AsyncExecutor

object App {

  @main
  def main(): Unit = {
    val db = Database.forConfig("rinhadb")
//    val db2 = Database.forURL(
//      "jdbc:postgresql://localhost:5432/rinha",
//      driver = "org.postgresql.Driver",
//      executor = AsyncExecutor("rinha", numThreads = 10, queueSize = 1000)
//    )


    val clientRepository: ClientRepository = InDiskClientRepository(db)
    val transactionRepository: TransactionRepository = InDiskTransactionRepository(db)
    
    val statementRepository: StatementRepository = InDiskStatementRepository(transactionRepository, clientRepository)

    val statementService = StatementService(statementRepository)
    val transactionService: TransactionService = TransactionService(transactionRepository, clientRepository)

    AppConfiguration(clientRepository, transactionService, statementService)
      .start(8080)
  }
}
