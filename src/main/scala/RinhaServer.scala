package io.andrelucas

import clients.ClientController
import transaction.InMemoryTransactionRepository

import zio.*
import zio.http.*

//https://www.baeldung.com/scala/zio-http
//https://zio.dev/guides/quickstarts/restful-webservice/
object RinhaServer extends ZIOAppDefault {
  
  
  override def run = 
    val httpControllers = ClientController()
    Server.
      serve(
        httpControllers.withDefaultErrorResponse
      )
      .provide(Server.defaultWithPort(8080),
        InMemoryTransactionRepository.layer
      )
}
