package io.andrelucas
package transaction

import zio.*

case class TransactionService():

  def createNew(clientId: Long, transactionRequest: TransactionRequest): ZIO[TransactionRepository, Throwable, TransactionResponse] =
    ZIO.serviceWithZIO[TransactionRepository]{ repo =>
      val newTransaction = transactionRequest.tipo match
        case "c" => Transaction.credit(transactionRequest.valor, transactionRequest.descricao, clientId)
        case "d" => Transaction.debit(transactionRequest.valor, transactionRequest.descricao, clientId)
      val result = repo.save(newTransaction)
      for {
        limit <- result.map(_._1)
        balance <- result.map(_._2)
      } yield TransactionResponse(limit.value, balance.value)
    }
