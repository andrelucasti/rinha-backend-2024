package io.andrelucas
package business.client

import business.client.{Balance, Client, Limit}

trait ClientRepository {
  def save(client: Client): Unit
  def findById(clientId: Long):Option[Client]
  def updateBalance(client: Client, balance: Balance):Unit
}

class InMemoryClientRepository extends ClientRepository:
  private val data = scala.collection.mutable.Map[Long, Client](
    2L -> Client(2, "Nberto", Balance(0,Limit(1000)))
  )

  override def save(client: Client): Unit = {
    data += (client.id -> client)
  }
  override def findById(clientId: Long): Option[Client] = {
    data.get(clientId)
  }

  override def updateBalance(client: Client, balance: Balance): Unit =
    val clientUpdated = client.copy(balance = balance)
    data += (clientUpdated.id -> clientUpdated)