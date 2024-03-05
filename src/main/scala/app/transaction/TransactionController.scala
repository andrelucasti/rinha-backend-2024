package io.andrelucas
package app.transaction

import business.client.{Client, ClientRepository}
import business.transaction.{LimitException, RequiredException, TenCharactersException, TransactionService}

import io.javalin.http.{Context, HttpStatus}

import scala.util.{Failure, Success}

case class TransactionController()
object TransactionController:

  def createNewTransaction(ctx: Context, 
                           clientRepository: ClientRepository, 
                           transactionService: TransactionService): Unit = {
    
    val clientId = ctx.pathParam("id")
    clientRepository.findById(clientId.toLong) match
      case None => ctx.status(HttpStatus.NOT_FOUND)
      case Some(client) =>

        val transactionRequest = TransactionRequest.fromJson(ctx.body())
        val transactionTuple = transactionService.createTransactionBy(client, transactionRequest)
        
        transactionTuple match
          case Failure(exception: LimitException) => ctx.status(HttpStatus.UNPROCESSABLE_CONTENT)
          case Failure(exception: RequiredException) => ctx.status(HttpStatus.UNPROCESSABLE_CONTENT)
          case Failure(exception: TenCharactersException) => ctx.status(HttpStatus.UNPROCESSABLE_CONTENT)
          case Failure(exception: UnsupportedOperationException) => ctx.status(HttpStatus.UNPROCESSABLE_CONTENT)
          case Failure(exception) => ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
          
          case Success(tuple) =>
            val response = tuple._1
            val transaction = tuple._2
            
            ctx.json(response.toJson).status(HttpStatus.OK)
            transactionService.save(transaction, client)
  }
