package io.andrelucas
package clients

import transaction.{TransactionRepository, TransactionRequest, TransactionService}

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
          case Right(transactionRequest) =>
            val client = ClientService().findById(id.toLong)
            TransactionService()
              .createNew(client, transactionRequest)
              .map(tResponse => Response.json(tResponse.toJson))

      } yield r).orDie
  }
  
  def apply(): Http[TransactionRepository, Throwable, Request, Response] = postHandler
}
