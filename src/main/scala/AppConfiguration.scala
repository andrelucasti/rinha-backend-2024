package io.andrelucas

import clients.ClientRepository
import transaction.TransactionController

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*

object AppConfiguration {

  def apply(clientRepository: ClientRepository) :Javalin = {
    val javalin = Javalin.create(config => {
      config.router.apiBuilder(() => {
        path("/clientes/{id}/transacoes", () => {
          post(ctx => TransactionController.createNewTransaction(ctx, clientRepository))
        })
      })
    })

    javalin.get("/", ctx => {
      ctx.result("Hello World")
    })

    javalin
  }
}
