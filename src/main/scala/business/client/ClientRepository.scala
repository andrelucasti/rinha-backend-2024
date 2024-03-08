package io.andrelucas
package business.client

trait ClientRepository {
  def save(client: Client): Unit
  def findById(clientId: Long):Option[Client]
}

class InMemoryClientRepository extends ClientRepository:
  private val data = scala.collection.mutable.Map[Long, Client](
    2L -> Client(2, "Nberto", Balance(0,Limit(1000)))
  )

  def save(client: Client): Unit = {
    data += (client.id -> client)
  }
  override def findById(clientId: Long): Option[Client] = {
    data.get(clientId)
  }