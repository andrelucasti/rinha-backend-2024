package io.andrelucas
package business.transaction

import business.client.{Balance, ClientRepository}
import business.statement.Statement

import scala.collection.mutable
import scala.util.Try

trait TransactionRepository {
  def findByClientId(clientId: Long): Try[Statement]
  def createTransaction(transaction: Transaction): Balance
}