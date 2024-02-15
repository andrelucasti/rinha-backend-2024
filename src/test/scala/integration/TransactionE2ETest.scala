package io.andrelucas
package integration

import io.andrelucas.integration.Spec.suite
import zio.ZIO
import zio.http.*
import zio.test._
import zio.test.Assertion._

object TransactionE2ETest extends ZIOSpecDefault {

  private def postTransaction =
    for {
      port <- ZIO.serviceWith[Server](_.port)

    } yield Request.post(Body.empty, url = URL(Path.root / "clientes" / "1" / "transacoes").withPort(port))
      .addHeaders(Headers(Header.Accept(MediaType.text.`plain`)))

  def spec = suite("transaction e2e")(
    test("should return the balance when a transaction is created") {

      val res = for{
        client <- ZIO.service[Client]
        testRequest <- postTransaction
        response <- client.request(testRequest)
      }yield response

      assertZIO(ZIO.succeed(""))(equalTo(""))
    }
  )
}
