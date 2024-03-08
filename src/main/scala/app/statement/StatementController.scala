package io.andrelucas
package app.statement

import business.client.ClientRepository
import business.statement.StatementService

import io.javalin.http.{Context, HttpStatus}

import scala.util.{Failure, Success}

case class StatementController()
object StatementController:

  def getStatement(ctx: Context,
                   clientRepository: ClientRepository,
                   statementService: StatementService): Context =

    val clientId = ctx.pathParam("id")
    val statementResponse = statementService.fetchStatementBy(clientId.toLong)

    statementResponse match
      case Failure(error) => ctx.status(HttpStatus.NOT_FOUND)
      case Success(statement) => ctx.json(statement.toJson).status(HttpStatus.OK)

