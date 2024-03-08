package io.andrelucas
package business.statement

import app.statement.{BalanceResponse, StatementResponse, StatementTransactionResponse}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.{Failure, Success, Try}

case class StatementService(statementRepository: StatementRepository):
  
  def fetchStatementBy(clientId: Long): Try[StatementResponse] = {
    
    val statement = statementRepository.findByClientId(clientId)
    
    statement match
      case Success(statement) => 
        val balanceResponse = BalanceResponse(statement.balance.total, statement.balance.limit.value, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        val transactionResponse = statement.transactions
          .map(t => StatementTransactionResponse(t.value, t.transactionType.prefix(), t.description, t.date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        
        Success(StatementResponse(balanceResponse, transactionResponse))
      
      case Failure(exception) => Failure(exception)
  }