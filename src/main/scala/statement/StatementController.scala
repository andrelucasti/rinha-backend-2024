package io.andrelucas
package statement

import clients.ClientRepository

import io.javalin.http.{Context, HttpStatus}

case class StatementController()
object StatementController:

  def getStatement(ctx: Context,
                   clientRepository: ClientRepository,
                   statementService: StatementService): Context =

    val clientId = ctx.pathParam("id")
    val clientOptional = clientRepository.findById(clientId.toLong)

    clientOptional match
      case None => ctx.status(HttpStatus.NOT_FOUND)
      case Some(client) =>
        val statementResponse = statementService.fetchStatementBy(clientId.toLong)
        ctx.json(statementResponse.toJson).status(HttpStatus.OK)

