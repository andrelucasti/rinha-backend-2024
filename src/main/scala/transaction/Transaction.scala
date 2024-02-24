package io.andrelucas
package transaction

import clients.{Balance, Limit}
import transaction.TransactionType.{CREDIT, DEBIT}

import scala.util.{Failure, Try}

case class Transaction(value: Long,
                       transactionType: TransactionType, 
                       description: String, 
                       clientId: Long)
object Transaction:
  def create: (Balance, Long, TransactionType, String, Long) => Try[Transaction] =
    (currentBalance: Balance, value: Long, transactionType: TransactionType, description: String, clientId: Long) =>
        transactionType match
          case CREDIT => Try(credit(currentBalance, value, description, clientId))
          case DEBIT => debit(currentBalance, value, description, clientId)
          case null => Failure(new UnsupportedOperationException("The transaction type should be c => Credit or d => Debit"))

private def credit(currentBalance: Balance,
                   transactionValue: Long,
                   description: String,
                   clientId: Long): Transaction =

  val newValue = currentBalance.total + transactionValue
  Transaction(newValue, CREDIT, description, clientId)

private def debit(currentBalance: Balance,
                  value: Long,
                  description: String,
                  clientId: Long): Try[Transaction] = Try {

  val newValue = currentBalance.total - value
  if newValue < currentBalance.limit.until then
    throw LimitException("The transaction value is greater than the limit.")

  Transaction(newValue, DEBIT, description, clientId)
}
