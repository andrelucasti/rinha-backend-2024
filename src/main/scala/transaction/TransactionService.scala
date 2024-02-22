package io.andrelucas
package transaction

import clients.Client

import zio.*

import scala.util.{Failure, Success}

case class TransactionService():
  def createNew(client: Client,
                transactionRequest: TransactionRequest): ZIO[TransactionRepository, Throwable, TransactionResponse] =
    ZIO.serviceWithZIO[TransactionRepository]{ repo =>
      Transaction.create(client.balance, transactionRequest.valor, TransactionType.fromString(transactionRequest.tipo), transactionRequest.descricao, client.id) match
        case Success(newTransaction) =>
          val result = repo.save(newTransaction)
          for {
            balance <- result.map(_.value)
          } yield TransactionResponse(client.limit.value, balance)

        case Failure(exception) =>
          ZIO.debug(s"Falha ao submeter transacao $exception")
          ZIO.fail(exception)
    }
