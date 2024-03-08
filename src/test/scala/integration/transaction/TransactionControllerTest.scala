package io.andrelucas
package integration.transaction

import io.andrelucas.app.transaction.{TransactionRequest, TransactionResponse}
import io.andrelucas.business.client.{Balance, Client, ClientRepository, InMemoryClientRepository, Limit}
import io.andrelucas.business.statement.{InMemoryStatementRepository, StatementRepository, StatementService}
import io.andrelucas.business.transaction.{InMemoryTransactionRepository, TransactionRepository, TransactionService}
import io.javalin.Javalin
import io.javalin.http.HttpStatus
import io.javalin.testtools.JavalinTest
import org.scalatest.Assertions
import org.scalatest.flatspec.AnyFlatSpec


class TransactionControllerTest extends AnyFlatSpec {
  val clientRepository: ClientRepository = InMemoryClientRepository()
  val transactionRepository: TransactionRepository = InMemoryTransactionRepository(clientRepository)
  val statementRepository: StatementRepository = InMemoryStatementRepository(transactionRepository, clientRepository)

  val statementService: StatementService = StatementService(statementRepository)
  val transactionService: TransactionService = TransactionService(transactionRepository, clientRepository)

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

      val response = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest("1000", "c", "credit 10").toJson)
      val transactionResponse = TransactionResponse.fromJson(response.body.string)

      Assertions.assert(response.code() == HttpStatus.OK.getCode)
      Assertions.assert(transactionResponse.limite == client.balance.limit.value)
      Assertions.assert(transactionResponse.saldo == 1000)
    })
  }

  it should "return the balance sum when two credit transaction happens with success" in {
    JavalinTest.test(app, (s, c) => {

      val client = Client(1, "Andre", Balance(0, Limit(1000)))
      clientRepository.save(client)

      c.post(s"/clientes/${client.id}/transacoes", TransactionRequest("1000", "c", "credit 10").toJson)

      val response = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest("1000", "c", "credit 10").toJson)
      val transactionResponse = TransactionResponse.fromJson(response.body.string)

      Assertions.assert(response.code() == HttpStatus.OK.getCode)
      Assertions.assert(transactionResponse.limite == client.balance.limit.value)
      Assertions.assert(transactionResponse.saldo == 2000)
    })
  }

  it should "subtract the balance when transaction is debit type" in {
    JavalinTest.test(app, (s, c) => {

      val client = Client(1, "Andre", Balance(0, Limit(1000)))
      clientRepository.save(client)

      c.post(s"/clientes/${client.id}/transacoes", TransactionRequest("1000", "c", "credit 10").toJson)

      val response = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest("500", "d", "credit 10").toJson)
      val transactionResponse = TransactionResponse.fromJson(response.body.string)

      Assertions.assert(response.code() == HttpStatus.OK.getCode)
      Assertions.assert(transactionResponse.limite == client.balance.limit.value)
      Assertions.assert(transactionResponse.saldo == 500)
    })
  }

  it should "return http status 422 when a limit is exceeded" in {
    JavalinTest.test(app, (s, c) => {
      val client = Client(1, "Andre", Balance(0, Limit(1000)))
      clientRepository.save(client)

      val response = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest("1001", "d", "debit 10").toJson)
      Assertions.assert(response.code() == HttpStatus.UNPROCESSABLE_CONTENT.getCode)
    })
  }

  it should "return http status 422 when description is null or empty " in {
    JavalinTest.test(app, (s, c) => {
      val client = Client(1, "Andre", Balance(0, Limit(1000)))
      clientRepository.save(client)

      val responseCreditEmpty = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest("1000", "c", "").toJson)
      Assertions.assert(responseCreditEmpty.code() == HttpStatus.UNPROCESSABLE_CONTENT.getCode)

      val responseDebitEmpty = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest("1000", "d", "").toJson)
      Assertions.assert(responseDebitEmpty.code() == HttpStatus.UNPROCESSABLE_CONTENT.getCode)

      val responseCreditNUll = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest("1000", "c", "null").toJson)
      Assertions.assert(responseCreditNUll.code() == HttpStatus.UNPROCESSABLE_CONTENT.getCode)

      val responseDebitNUll = c.post(s"/clientes/${client.id}/transacoes", TransactionRequest("1000", "d", "null").toJson)
      Assertions.assert(responseDebitNUll.code() == HttpStatus.UNPROCESSABLE_CONTENT.getCode)
    })
  }

  it should "return http status 422 when description is great than 10 characters" in {
    JavalinTest.test(app, (s, c) => {
      val client = Client(1, "Andre", Balance(0, Limit(1000)))
      clientRepository.save(client)

      val payload = s"""{"valor": 1000, "tipo": "d", "descricao": "maiorque10000000"}"""
      val response = c.post(s"/clientes/${client.id}/transacoes", payload)

      Assertions.assert(response.code() == HttpStatus.UNPROCESSABLE_CONTENT.getCode)
    })
  }

  it should "return http status 400 when value is a float" in {
    JavalinTest.test(app, (s, c) => {
      val client = Client(1, "Andre", Balance(0, Limit(1000)))
      clientRepository.save(client)

      val payload = s"""{"valor": 1.2, "tipo": "d", "descricao": "huehuebr"}"""

      val response = c.post(s"/clientes/${client.id}/transacoes", payload)
      Assertions.assert(response.code() == HttpStatus.UNPROCESSABLE_CONTENT.getCode)
    })
  }
}
