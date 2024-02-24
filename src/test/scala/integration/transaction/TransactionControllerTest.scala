package io.andrelucas
package integration.transaction

import clients.*
import transaction.*

import io.andrelucas.statement.{InMemoryStatementRepository, StatementRepository, StatementService}
import io.javalin.Javalin
import io.javalin.http.HttpStatus
import io.javalin.testtools.JavalinTest
import org.scalatest.Assertions
import org.scalatest.flatspec.AnyFlatSpec


class TransactionControllerTest extends AnyFlatSpec {
  val clientRepository: ClientRepository = InMemoryClientRepository()

  val statementRepository: StatementRepository = InMemoryStatementRepository(clientRepository)
  val statementService: StatementService = StatementService(clientRepository, statementRepository)

  val transactionRepository: TransactionRepository = InMemoryTransactionRepository()
  val transactionService: TransactionService = TransactionService(transactionRepository, statementRepository, clientRepository)

  val app: Javalin = AppConfiguration(clientRepository, transactionService, statementService)

  it should "return http status 404 when a client is not found" in {
    JavalinTest.test(app, (s, c) => {
      Assertions.assert(c.post("/clientes/1/transacoes").code() == HttpStatus.NOT_FOUND.getCode)
    })
  }

  it should "return the limit and balance when transaction happens with success" in {
    JavalinTest.test(app, (s, c) => {

      val client = Client(1, "Andre", Balance(0, Limit(1000)))
      clientRepository.save(client)

      val response = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest(1000, "c", "credit 10").toJson)
      val transactionResponse = TransactionResponse.fromJson(response.body.string)

      Assertions.assert(response.code() == HttpStatus.OK.getCode)
      Assertions.assert(transactionResponse.limit == client.balance.limit.value)
      Assertions.assert(transactionResponse.balance == 1000)
    })
  }

  it should "return the balance sum when two credit transaction happens with success" in {
    JavalinTest.test(app, (s, c) => {

      val client = Client(1, "Andre", Balance(0, Limit(1000)))
      clientRepository.save(client)

      c.post(s"/clientes/${client.id}/transacoes", TransactionRequest(1000, "c", "credit 10").toJson)

      val response = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest(1000, "c", "credit 10").toJson)
      val transactionResponse = TransactionResponse.fromJson(response.body.string)

      Assertions.assert(response.code() == HttpStatus.OK.getCode)
      Assertions.assert(transactionResponse.limit == client.balance.limit.value)
      Assertions.assert(transactionResponse.balance == 2000)
    })
  }

  it should "subtract the balance when transaction is debit type" in {
    JavalinTest.test(app, (s, c) => {

      val client = Client(1, "Andre", Balance(0, Limit(1000)))
      clientRepository.save(client)

      c.post(s"/clientes/${client.id}/transacoes", TransactionRequest(1000, "c", "credit 10").toJson)

      val response = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest(500, "d", "credit 10").toJson)
      val transactionResponse = TransactionResponse.fromJson(response.body.string)

      Assertions.assert(response.code() == HttpStatus.OK.getCode)
      Assertions.assert(transactionResponse.limit == client.balance.limit.value)
      Assertions.assert(transactionResponse.balance == 500)
    })
  }

  it should "return http status 422 when a limit is exceeded" in {
    JavalinTest.test(app, (s, c) => {
      val client = Client(1, "Andre", Balance(0, Limit(1000)))
      clientRepository.save(client)

      val response = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest(1001, "d", "debit 10").toJson)
      Assertions.assert(response.code() == HttpStatus.UNPROCESSABLE_CONTENT.getCode)
    })
  }
}
