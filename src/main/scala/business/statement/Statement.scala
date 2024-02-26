package io.andrelucas
package business.statement

import business.client.Balance
import io.andrelucas.business.transaction.Transaction

import java.time.LocalDateTime

case class Statement(balance: Balance,
                     transactions: List[Transaction],
                     statementDate: LocalDateTime = LocalDateTime.now())
