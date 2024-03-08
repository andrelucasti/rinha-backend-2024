package io.andrelucas

import io.andrelucas.app.statement.StatementController
import io.andrelucas.app.transaction.TransactionController
import io.andrelucas.business.client.ClientRepository
import io.andrelucas.business.statement.StatementService
import io.andrelucas.business.transaction.TransactionService
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*

object AppConfiguration {
  def apply(clientRepository: ClientRepository,
            transactionService: TransactionService,
            statementService: StatementService) :Javalin = {
    val javalin = Javalin.create(config => {
      config.useVirtualThreads = true
      config.router.apiBuilder(() => {
        path("/clientes/{id}/transacoes", () => {
          post(ctx => {
            TransactionController.createTransaction(ctx, transactionService)
          })
        })
        path("/clientes/{id}/extrato", () => {
          get(ctx => {
            StatementController.getStatement(ctx, clientRepository, statementService)
          })
        })
      })
    })

    javalin.get("/", ctx => {
      ctx.result("Hello World")
    })

    javalin
  }
}
