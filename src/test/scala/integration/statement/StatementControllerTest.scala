package io.andrelucas
package integration.statement

import io.andrelucas.app.statement.StatementResponse
import io.andrelucas.app.transaction.TransactionRequest

import io.andrelucas.business.client.{Balance, Client, ClientRepository, InMemoryClientRepository, Limit}
import io.andrelucas.business.statement.{InMemoryStatementRepository, StatementRepository, StatementService}
import io.andrelucas.business.transaction.{InMemoryTransactionRepository, TransactionRepository, TransactionService}
import io.javalin.Javalin
import io.javalin.http.HttpStatus
import io.javalin.testtools.JavalinTest
import org.scalatest.Assertions
import org.scalatest.flatspec.AnyFlatSpec

class StatementControllerTest extends AnyFlatSpec {
  val clientRepository: ClientRepository = InMemoryClientRepository()

  val statementRepository: StatementRepository = InMemoryStatementRepository(clientRepository)
  val statementService: StatementService = StatementService(clientRepository, statementRepository)

  val transactionRepository: TransactionRepository = InMemoryTransactionRepository()
  val transactionService: TransactionService = TransactionService(transactionRepository, statementRepository, clientRepository)

  val app: Javalin = AppConfiguration(clientRepository, transactionService, statementService)

  it should "get the client statement" in {
    JavalinTest.test(app, (s,c) =>{
      val client = Client(1, "Andre", Balance(0, Limit(1000)))
      clientRepository.save(client)

      c.post(s"/clientes/${client.id}/transacoes", TransactionRequest(1000, "c", "credit 10").toJson)
      c.post(s"/clientes/${client.id}/transacoes", TransactionRequest(1000, "c", "credit 10").toJson)
      c.post(s"/clientes/${client.id}/transacoes", TransactionRequest(1000, "c", "credit 10").toJson)

      val response = c.get(s"/clientes/${client.id}/extrato")
      Assertions.assert(response.code() == HttpStatus.OK.getCode)

      val statementResponse = StatementResponse.fromJson(response.body.string)
      Assertions.assert(statementResponse.saldo.total == 3000)
      Assertions.assert(statementResponse.saldo.limite == 1000)
      Assertions.assert(statementResponse.ultimas_transacoes.size == 3)
    })
  }

  it should "return http status 404 when a client is not found" in {
    JavalinTest.test(app, (s, c) => {
      Assertions.assert(c.post("/clientes/1/extrato").code() == HttpStatus.NOT_FOUND.getCode)
    })
  }
}
