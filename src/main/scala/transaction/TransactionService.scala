package io.andrelucas
package transaction

import clients.{Client, ClientRepository}
import statement.StatementRepository

import scala.util.Try

case class TransactionService(transactionRepository: TransactionRepository, 
                              statementRepository: StatementRepository,
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
    statementRepository.save(transaction)

    val balance = transaction.transactionType.newBalance(client)
    clientRepository.updateBalance(client, balance)
  }