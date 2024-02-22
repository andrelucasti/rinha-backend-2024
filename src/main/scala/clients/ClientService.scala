package io.andrelucas
package clients

case class ClientService():
  def findById(id: Long):Client =
    Client(id, "Andre", Limit(1000000), Balance(10000))
