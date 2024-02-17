package io.andrelucas
package integration

import integration.Spec.suite
import transaction.{InMemoryTransactionRepository, TransactionRequest, TransactionResponse, TransactionService}

import zio.ZIO
import zio.test.*
import zio.test.Assertion.equalTo

object TransactionServiceIntegrationTest extends ZIOSpecDefault {

  def spec = suite("Transaction")(
    test("should return the balance when a transaction is created") {
      val request = TransactionRequest(10000, "c", "credit 100")
      val response = TransactionService().createNew(1, request)

      assertZIO(response.map(_.balance))(equalTo(10000))

    }.provideSome(
      InMemoryTransactionRepository.layer
    ),

    test("should return the limit when a transaction is created") {
      val request = TransactionRequest(10000, "c", "credit 100")
      val response = TransactionService().createNew(1, request)

      assertZIO(response.map(_.limit))(equalTo(10000))
    }.provideSome(
      InMemoryTransactionRepository.layer
    ),
  )

}
