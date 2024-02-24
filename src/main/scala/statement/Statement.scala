package io.andrelucas
package statement

import clients.Balance
import transaction.Transaction

import java.time.LocalDateTime

case class Statement(balance: Balance,
                     transactions: List[Transaction],
                     statementDate: LocalDateTime = LocalDateTime.now())
