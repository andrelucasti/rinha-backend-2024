package io.andrelucas
package business.transaction

import app.transaction.{TransactionRequest, TransactionResponse}
import business.client.{Client, ClientRepository}
import business.transaction

import scala.util.Try

case class TransactionService(transactionRepository: TransactionRepository,
                              clientRepository: ClientRepository):

  def createTransaction(clientId: Long,
                        transactionRequest: TransactionRequest): Try[TransactionResponse] = {

    Transaction.create(transactionRequest.valor,
      transactionRequest.tipo,
      transactionRequest.descricao, clientId).map { newTransaction =>
        val balance = transactionRepository.createTransaction(newTransaction)
        TransactionResponse(balance.limit.value, balance.total)
      }
  }

  def createTransactionBy(client: Client, 
                          transactionRequest: TransactionRequest): Try[(TransactionResponse, Transaction)] =
    Transaction.create(
      transactionRequest.valor,
      transactionRequest.tipo,
      transactionRequest.descricao,
      client.id).map { newTransaction =>

      val newBalance = newTransaction.transactionType.newBalance(client)

      (TransactionResponse(newBalance.limit.value, newBalance.total), newTransaction)
    }