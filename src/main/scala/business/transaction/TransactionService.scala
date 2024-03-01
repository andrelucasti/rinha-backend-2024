package io.andrelucas
package business.transaction

import app.transaction.{TransactionRequest, TransactionResponse}
import business.client.{Client, ClientRepository}

import scala.util.Try

case class TransactionService(transactionRepository: TransactionRepository,
                              clientRepository: ClientRepository):
  def createTransactionBy(client: Client, 
                          transactionRequest: TransactionRequest): Try[(TransactionResponse, Transaction)] =

    if transactionRequest.descricao.isEmpty
    then throw RequiredException("Description is required")
      
    Transaction.create(
      transactionRequest.valor,
      transactionRequest.tipo,
      transactionRequest.descricao,
      client.id).map { newTransaction =>

      val newBalance = newTransaction.transactionType.newBalance(client)
      
      (TransactionResponse(newBalance.limit.value, newBalance.total), newTransaction)
    }
    
  
  def save(transaction: Transaction, 
           client: Client): Unit = {
    
    transactionRepository.save(transaction)

    val balance = transaction.transactionType.newBalance(client)
    clientRepository.updateBalance(client, balance)
  }