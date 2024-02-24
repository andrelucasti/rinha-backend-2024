package io.andrelucas
package transaction

import io.andrelucas.clients._
import io.andrelucas.transaction

trait TransactionType:
  def newBalance(client: Client): Balance
  def prefix(): String
  
case class CreditTransaction(value: Long) extends TransactionType:
  override def newBalance(client: Client): Balance =
    val newBalance = client.balance.total + value
    
    Balance(newBalance, client.balance.limit)

  override def prefix(): String = "c"
case class DebitTransaction(value: Long) extends TransactionType:
  override def newBalance(client: Client): Balance =
    val newBalance = client.balance.total - value
    
    if newBalance < client.balance.limit.until then 
      throw LimitException(s"the client ${client.id} limit was exceeded")
    
    Balance(newBalance, client.balance.limit)

  override def prefix(): String = "d"

