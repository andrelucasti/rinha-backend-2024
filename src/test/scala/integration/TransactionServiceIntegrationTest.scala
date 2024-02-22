package io.andrelucas
package integration

import integration.Spec.suite
import transaction.{InMemoryTransactionRepository, TransactionRequest, TransactionResponse, TransactionService}

import io.andrelucas.clients.{Balance, Client, Limit}
import zio.ZIO
import zio.test.*
import zio.test.Assertion.equalTo

object TransactionServiceIntegrationTest extends ZIOSpecDefault {

  def spec = suite("Transaction")(
    test("should return the sum of the balance when happens a new credit transaction") {
      val client = Client(1, "Andre", Limit(10000), Balance(100))
      val response = TransactionService().createNew(client, TransactionRequest(2000, "c", "credit 200"))

      assertZIO(response.map(_.balance))(equalTo(2100))
    }.provideSome(
      InMemoryTransactionRepository.layer
    ),
    test("should return the subtract of the balance when happens a new debit transaction") {
      val client = Client(1, "Andre", Limit(10000), Balance(3000))
      val response = TransactionService().createNew(client, TransactionRequest(2000, "d", "credit 200"))

      assertZIO(response.map(_.balance))(equalTo(1000))
    }.provideSome(
      InMemoryTransactionRepository.layer
    ),

    test("should not have limit changes when happens a new credit transaction") {
      val client = Client(1, "Andre", Limit(10000), Balance(3000))
      val response = TransactionService().createNew(client, TransactionRequest(2000, "c", "credit 200"))

      assertZIO(response.map(_.limit))(equalTo(10000))
    }.provideSome(
      InMemoryTransactionRepository.layer
    ),

    test("should not have limit changes when happens a new debit transaction") {
      val client = Client(1, "Andre", Limit(70000), Balance(3000))
      val response = TransactionService().createNew(client, TransactionRequest(2000, "c", "credit 200"))

      assertZIO(response.map(_.limit))(equalTo(70000))
    }.provideSome(
      InMemoryTransactionRepository.layer
    ),
  )
}
