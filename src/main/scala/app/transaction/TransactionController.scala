package io.andrelucas
package app.transaction

import business.client.{Client, ClientRepository}
import business.transaction.{LimitException, RequiredException, TenCharactersException, TransactionService}

import io.javalin.http.{Context, HttpStatus}

import scala.util.{Failure, Success}

case class TransactionController()
object TransactionController:
  
  def createTransaction(ctx: Context, 
                        transactionService: TransactionService): Unit = {

    val clientId = ctx.pathParam("id")
    val transactionRequest = TransactionRequest.fromJson(ctx.body())
    
    transactionService.createTransaction(clientId.toLong, transactionRequest) match
      case Failure(exception: LimitException) =>
        println(exception.getMessage)
        ctx.status(HttpStatus.UNPROCESSABLE_CONTENT)
      case Failure(exception: RequiredException) =>
        println(exception.getMessage)
        ctx.status(HttpStatus.UNPROCESSABLE_CONTENT)
      case Failure(exception: TenCharactersException) =>
        println(exception.getMessage)
        ctx.status(HttpStatus.UNPROCESSABLE_CONTENT)
      case Failure(exception: UnsupportedOperationException) =>
        println(exception.getMessage)
        ctx.status(HttpStatus.UNPROCESSABLE_CONTENT)

      case Failure(exception) =>
        exception.printStackTrace()
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
      
      case Success(response) =>
        ctx.json(response.toJson).status(HttpStatus.OK)
  }