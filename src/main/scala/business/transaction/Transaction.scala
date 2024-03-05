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

  private val valorPattern = "^\\d+$"
  def create(value: String,
             transactionType: String, 
             description: String, 
             clientId: Long): Try[Transaction] =
    
    
    Try {
      val valueAsLong = if value.matches(valorPattern) then
        value.toLong
      else
        throw RequiredException("Value should be a number")

      if description.isEmpty || description == null || description == "null"
      then throw RequiredException("Description is required")

      if description.length > 10
      then throw TenCharactersException("Got an error at description size")

      val t = transactionType match
        case "c" => CreditTransaction(valueAsLong)
        case "d" => DebitTransaction(valueAsLong)
        case _ => throw UnsupportedOperationException("transaction type should be 'c' -> CREDIT or 'd' -> DEBIT")
      
      Transaction(valueAsLong, t, description, clientId, LocalDateTime.now())
    }


  def restore(value: Long, transactionType: String, description: String, clientId: Long, date: LocalDateTime): Transaction =
    val t = transactionType match
      case "c" => CreditTransaction(value)
      case "d" => DebitTransaction(value)
      
    Transaction(value, t, description, clientId, date)  