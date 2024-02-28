package io.andrelucas
package repository.transaction

import business.transaction.Transaction

import slick.jdbc.PostgresProfile.api.*

import java.time.LocalDateTime

sealed case class TransactionEntity(id: Option[Long],
                                    value: Long,
                                    transactionType: String,
                                    description: String,
                                    clientId: Long,
                                    updatedAt: Option[LocalDateTime]):
  
  def toTransaction: Transaction =
    Transaction.restore(value, transactionType, description, clientId, updatedAt.get)

object TransactionEntity:
  def fromTransaction(tr: Transaction): TransactionEntity =
    TransactionEntity(None, tr.value, tr.transactionType.prefix(), tr.description, tr.clientId, Some(LocalDateTime.now()))

case class TransactionTable(tag: Tag) extends Table[TransactionEntity](tag, "transactions") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def description = column[String]("description")
  def value = column[Long]("value")
  def transactionType: Rep[String] = column[String]("transaction_type")
  def clientId = column[Long]("client_id")
  def updatedAt = column[LocalDateTime]("updated_at", O.Default(LocalDateTime.now()))

  override def * = (id.?, value, transactionType, description, clientId, updatedAt.?) <> ((TransactionEntity.apply _).tupled, TransactionEntity.unapply)

}

val transactionTable = TableQuery[TransactionTable]
