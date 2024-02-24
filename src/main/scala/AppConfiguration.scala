package io.andrelucas

import clients.ClientRepository
import transaction.{TransactionController, TransactionService}

import io.andrelucas.statement.{StatementController, StatementService}
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*

object AppConfiguration {
  def apply(clientRepository: ClientRepository,
            transactionService: TransactionService,
            statementService: StatementService) :Javalin = {
    val javalin = Javalin.create(config => {
      config.router.apiBuilder(() => {
        path("/clientes/{id}/transacoes", () => {
          post(ctx => TransactionController.createNewTransaction(ctx, clientRepository, transactionService))
        })
        path("/clientes/{id}/extrato", () => {
          get(ctx => StatementController.getStatement(ctx, clientRepository, statementService))
        })
      })
    })

    javalin.get("/", ctx => {
      ctx.result("Hello World")
    })

    javalin
  }
}
