package io.andrelucas
package transaction

import transaction.TransactionType.{CREDIT, DEBIT}

import io.andrelucas.clients.Balance

import scala.util.{Failure, Try}

case class Transaction(value: Long,
                       transactionType: TransactionType, 
                       description: String, 
                       clientId: Long)
object Transaction:
  def credit (currentBalance: Balance, transactionValue: Long, description: String, clientId: Long): Transaction =
    val newValue = currentBalance.value + transactionValue
    Transaction(newValue, CREDIT, description, clientId)

  def debit (currentBalance: Balance, value: Long, description: String, clientId: Long): Transaction =
    val newValue = currentBalance.value - value
    Transaction(newValue, DEBIT, description, clientId)

  def create: (Balance, Long, TransactionType, String, Long) => Try[Transaction] =
    (currentBalance: Balance, value: Long, transactionType: TransactionType, description: String, clientId: Long) =>
        transactionType match
          case CREDIT => Try(credit(currentBalance, value, description, clientId))
          case DEBIT => Try(debit(currentBalance, value, description, clientId))
          case null => Failure(new UnsupportedOperationException("The transaction type should be c => Credit or d => Debit"))