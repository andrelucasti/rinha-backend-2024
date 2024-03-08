package io.andrelucas
package business.statement

import business.client.ClientRepository
import business.transaction.TransactionRepository

import scala.util.Try

trait StatementRepository {
  def findByClientId(clientId: Long): Try[Statement]
}