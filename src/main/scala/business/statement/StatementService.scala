package io.andrelucas
package business.statement

import app.statement.{BalanceResponse, StatementResponse, StatementTransactionResponse}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class StatementService(statementRepository: StatementRepository):
  
  def fetchStatementBy(clientId: Long): StatementResponse = {
    
    val statement = statementRepository.findByClientId(clientId)
    val balanceResponse = BalanceResponse(statement.balance.total, 
      statement.balance.limit.value, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
    
    val transactionResponse = statement.transactions
      .map(t => StatementTransactionResponse(t.value, t.transactionType.prefix(), t.description, t.date.format(DateTimeFormatter.ISO_DATE_TIME)))
    
    StatementResponse(balanceResponse, transactionResponse)
  }
