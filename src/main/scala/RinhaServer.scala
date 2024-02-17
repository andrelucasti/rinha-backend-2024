package io.andrelucas

import io.andrelucas.clients.ClientController
import io.andrelucas.transaction.{InMemoryTransactionRepository, TransactionService}
import zio.*
import zio.http.*
import zio.http.Server

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
