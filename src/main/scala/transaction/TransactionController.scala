package io.andrelucas
package transaction

import clients.ClientRepository

import io.javalin.http.{Context, HttpStatus}

import scala.util.{Failure, Success}

case class TransactionController()
object TransactionController:
  // - /clientes/{id}/transacoes"
  def createNewTransaction(ctx: Context, clientRepository: ClientRepository): Unit = {

    val clientId = ctx.pathParam("id")
    clientRepository.findById(clientId.toLong) match
      case None => ctx.status(HttpStatus.NOT_FOUND)
      case Some(client) =>
        val transactionRequest = TransactionRequest.fromJson(ctx.body())
        val newTransaction = Transaction.create(
          client.balance,
          transactionRequest.valor,
          TransactionType.fromString(transactionRequest.tipo),
          transactionRequest.descricao,
          client.id)

        newTransaction match
          case Success(transaction) => ctx.json(TransactionResponse(client.balance.limit.value, transaction.value).toJson).status(HttpStatus.OK)
          case Failure(exception: LimitException) => ctx.status(HttpStatus.UNPROCESSABLE_CONTENT)
          case Failure(exception) => ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)

  }
