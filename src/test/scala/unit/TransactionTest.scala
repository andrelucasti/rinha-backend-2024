package io.andrelucas
package unit

import transaction.{Transaction, TransactionType}

import zio.ZIO
import zio.test._

object TransactionTest extends ZIOSpecDefault {
  def spec = suite("TransactionTest")(

    test("should create a credit transaction") {
      val transaction = ZIO.succeed(
        Transaction.credit(1000, "Credit Transaction", 1)
      )

      assertZIO(transaction.map(_.transactionType))(Assertion.equalTo(TransactionType.CREDIT))
    },

    test("shoud create a debit transaction") {
      val transaction = ZIO.succeed(
        Transaction.debit(1000, "Credit Transaction", 1)
      )

      assertZIO(transaction.map(_.transactionType))(Assertion.equalTo(TransactionType.DEBIT))
    }
  )
}
