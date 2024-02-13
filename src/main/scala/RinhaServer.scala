package io.andrelucas

import zio.http._
import zio._

object RinhaServer extends ZIOAppDefault {
  val app = Routes(
    Method.GET / "text" -> handler(Response.text("Hello World"))
  ).toHttpApp

  override def run = Server.serve(app).provide(Server.default)
}
