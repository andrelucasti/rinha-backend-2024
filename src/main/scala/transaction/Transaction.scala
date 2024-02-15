package io.andrelucas
package transaction

import transaction.TransactionType.{CREDIT, DEBIT}

case class Transaction(value: Int, 
                       transactionType: TransactionType, 
                       description: String, 
                       clientId: Long)
object Transaction:
  def credit (value: Int, description: String, clientId: Long): Transaction = Transaction(value, CREDIT, description, clientId)
  def debit (value: Int, description: String, clientId: Long): Transaction = Transaction(value, DEBIT, description, clientId)