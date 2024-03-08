package io.andrelucas
package business.client

trait ClientRepository {
  def save(client: Client): Unit
  def findById(clientId: Long):Option[Client]
}