package io.andrelucas
package business.transaction

import java.time.LocalDateTime
import scala.util.Try

case class Transaction(value: Long,
                       transactionType: TransactionType, 
                       description: String, 
                       clientId: Long,
                       date: LocalDateTime)
object Transaction:
  def create(value: Long, 
             transactionType: String, 
             description: String, 
             clientId: Long): Try[Transaction] =
    
    
    Try {
      val t = transactionType match
        case "c" => CreditTransaction(value)
        case "d" => DebitTransaction(value)
        case _ => throw UnsupportedOperationException("transactio type should be 'c' -> CREDIT or 'd' -> DEBIT")
      
      Transaction(value, t, description, clientId, LocalDateTime.now())
    }

