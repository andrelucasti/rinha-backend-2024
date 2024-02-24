package io.andrelucas
package clients

trait ClientRepository {
  def save(client: Client): Unit
  def findById(clientId: Long):Option[Client]
}

class InMemoryClientRepository extends ClientRepository:
  private val data = scala.collection.mutable.Map[Long, Client]()

  override def save(client: Client): Unit = {
    data += (client.id -> client)
  }
  override def findById(clientId: Long): Option[Client] = {
    data.get(clientId)
  }