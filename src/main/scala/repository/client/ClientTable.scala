package io.andrelucas
package repository.client

import business.client.{Balance, Client, Limit}

import slick.jdbc.PostgresProfile.api.*

import java.time.LocalDateTime

sealed case class ClientEntity(id: Long, name: String, limit: Long, balance: Long, updatedAt: Option[LocalDateTime])

object ClientEntity:
  def fromClient(c: Client): ClientEntity =
    ClientEntity(c.id, c.name, c.balance.limit.value, c.balance.total, Some(LocalDateTime.now()))
  
  extension (entity: ClientEntity)
    def toClient: Client =
      Client(entity.id, entity.name, Balance(entity.balance, Limit(entity.limit)))
      

case class ClientTable(tag: Tag) extends Table[ClientEntity](tag, "clientes") {
  def id = column[Long]("id", O.PrimaryKey)
  def name = column[String]("name")
  def limit = column[Long]("limit")
  def balance = column[Long]("balance")
  def updatedAt = column[LocalDateTime]("updated_at", O.Default(LocalDateTime.now()))

  override def * = (id, name, limit, balance, updatedAt.?) <> ((ClientEntity.apply _).tupled, ClientEntity.unapply) 
}

val clientTable = TableQuery[ClientTable]
