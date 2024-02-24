package io.andrelucas

import clients.{ClientRepository, InMemoryClientRepository}
import statement.{InMemoryStatementRepository, StatementRepository, StatementService}
import transaction.{InMemoryTransactionRepository, TransactionRepository, TransactionService}

object App {

  @main
  def main(): Unit = {
    val clientRepository: ClientRepository = InMemoryClientRepository()
    
    val statementRepository: StatementRepository = InMemoryStatementRepository(clientRepository)
    val statementService = StatementService(clientRepository, statementRepository)
    
    val transactionRepository: TransactionRepository = InMemoryTransactionRepository()
    val transactionService: TransactionService = TransactionService(transactionRepository, statementRepository, clientRepository)
    
    AppConfiguration(clientRepository, transactionService, statementService)
      .start(7070)
  }
}
