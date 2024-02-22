package io.andrelucas
package unit

import clients.Balance
import transaction.TransactionType.{CREDIT, DEBIT}
import transaction.{Transaction, TransactionType}

import zio.ZIO
import zio.test.*

object TransactionTest extends ZIOSpecDefault {
  def spec = suite("TransactionTest")(
    test("should create a credit transaction") {
      val transaction = ZIO.succeed(
        Transaction.create(Balance(1000), 1000, CREDIT, "creditTransaction", 1L)
      )

      assertZIO(transaction.map(_.get.transactionType))(Assertion.equalTo(TransactionType.CREDIT))
    },

    test("should create a debit transaction") {
      val transaction = ZIO.succeed(
        Transaction.debit(Balance(1000), 1000, "DEBIT Transaction", 1)
      )

      assertZIO(transaction.map(_.transactionType))(Assertion.equalTo(TransactionType.DEBIT))
    },

    test("should sum the client balance when a transaction type is credit") {
      val transaction = ZIO.succeed {
        Transaction.create(Balance(1000), 1000, CREDIT, "creditTransaction", 1L).get
      }

      assertZIO(transaction.map(_.value))(Assertion.equalTo(2000))
    },

    test("should subtract the client balance when a transaction type is debit") {
      val transaction = ZIO.succeed {
        Transaction.create(Balance(3000), 1000, DEBIT, "creditTransaction", 1L).get
      }

      assertZIO(transaction.map(_.value))(Assertion.equalTo(2000))
    },
  )
}
