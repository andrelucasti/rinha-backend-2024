package io.andrelucas
package integration.transaction

import clients.*
import statement.{InMemoryStatementRepository, StatementRepository, StatementService}
import transaction.{InMemoryTransactionRepository, TransactionRepository, TransactionRequest, TransactionService}

import org.scalatest.Assertions
import org.scalatest.flatspec.AnyFlatSpec

class TransactionServiceIntegrationTest extends AnyFlatSpec {
  val clientRepository: ClientRepository = InMemoryClientRepository()

  val statementRepository: StatementRepository = InMemoryStatementRepository(clientRepository)
  val statementService: StatementService = StatementService(clientRepository, statementRepository)

  val transactionRepository: TransactionRepository = InMemoryTransactionRepository()
  
  val subject: TransactionService = TransactionService(transactionRepository, statementRepository, clientRepository)

  it should "return add new balance when transaction is credit" in {
    val client = Client(1, "Andre", balance = Balance(500, Limit(2000)))
    val transactionRequest = TransactionRequest(1000, "c", "new credit value")

    val tuple = subject.createTransactionBy(client, transactionRequest).get
    val response = tuple._1
    val transaction = tuple._2

    Assertions.assert(transaction.transactionType.prefix() == "c")
    Assertions.assert(transaction.clientId == client.id)
    Assertions.assert(transaction.value == 1000)

    Assertions.assert(response.limit == 2000)
    Assertions.assert(response.balance == 1500)

  }
}
