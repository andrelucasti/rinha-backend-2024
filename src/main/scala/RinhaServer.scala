package io.andrelucas

import io.andrelucas.clients.ClientController
import zio.ZIOAppDefault
import zio.http.Server


object RinhaServer extends ZIOAppDefault {
  
  override def run = Server.serve(ClientController().withDefaultErrorResponse)
    .provide(Server.defaultWithPort(8080))
}
