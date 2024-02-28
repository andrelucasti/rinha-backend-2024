package io.andrelucas
package business.transaction

import io.andrelucas.app.transaction.{TransactionRequest, TransactionResponse}
import io.andrelucas.business.client.{Client, ClientRepository}
import io.andrelucas.business.statement.StatementRepository

import scala.util.Try

case class TransactionService(transactionRepository: TransactionRepository,
                              clientRepository: ClientRepository):
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
    
  
  def save(transaction: Transaction, client: Client): Unit = {
    transactionRepository.save(transaction)

    val balance = transaction.transactionType.newBalance(client)
    clientRepository.updateBalance(client, balance)
  }