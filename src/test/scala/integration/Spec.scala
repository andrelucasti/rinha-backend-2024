package io.andrelucas
package integration

import zio.http.{Handler, Request, Response, Root, URL}
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assertZIO}

object Spec extends ZIOSpecDefault {

  def spec = suite("http") (
    test("should be ok"){
      val app = Handler.ok.toHttpApp
      val req = Request.get(URL(Root))

      assertZIO(app.runZIO(req))(equalTo(Response.ok))
    }
  )
}
