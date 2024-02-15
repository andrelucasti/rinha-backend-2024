package io.andrelucas
package clients

import io.andrelucas.transaction.TransactionRequest
import zio.ZIO
import zio.http.*
import zio.json.*

object ClientController {

  private def postHandler  = Http.collectZIO[Request] {
    case req @ Method.POST -> Root / "clientes" / id / "transacoes" =>
      (for {
        transactionRequest <- req.body.asString.map(_.fromJson[TransactionRequest])
        r <- transactionRequest match
          case Left(e) => ZIO.debug(s"Falha ao submeter transacao $e").as(Response.text(e).withStatus(Status.BadRequest))
          case Right(transactionRequest) => ZIO.succeed(
            Response.json(transactionRequest.toJsonPretty)
          )
      } yield r).orDie
  }
  
  def apply(): Http[Any, Throwable, Request, Response] = postHandler
}
