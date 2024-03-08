package io.andrelucas
package unit

import business.client.{Balance, Client, Limit}
import business.transaction.Transaction

import org.scalatest.Assertions
import org.scalatest.flatspec.AnyFlatSpec

class TransactionTest extends AnyFlatSpec {
  it should "return a transaction of type credit" in {
    val transaction = Transaction.create("1000", "c", "desc", 1).get

    Assertions.assert(transaction.transactionType.prefix() == "c")
    Assertions.assert(transaction.value == 1000)
  }
  it should "return a transaction of type debit" in {
    val transaction = Transaction.create("2000", "d", "desc", 1).get

    Assertions.assert(transaction.transactionType.prefix() == "d")
    Assertions.assert(transaction.value == 2000)
  }
  it should "return exception when transaction type is invalid" in {
    val transaction = Transaction.create("2000", "x", "desc", 1)

    Assertions.assert(transaction.isFailure)
    Assertions.assert(transaction.failed.get.getMessage == "transaction type should be 'c' -> CREDIT or 'd' -> DEBIT")
  }
  it should "return exception when description is null or empty" in {
    val transaction = Transaction.create("2000", "c", "", 1)

    Assertions.assert(transaction.isFailure)
    Assertions.assert(transaction.failed.get.getMessage == "Description is required")

    val transaction2 = Transaction.create("2000", "c", null, 1)

    Assertions.assert(transaction2.isFailure)
    Assertions.assert(transaction2.failed.get.getMessage == "Description is required")
  }

  it should "return exception when value is float" in {
    val transaction = Transaction.create("2.6666", "c", "desc", 1)

    Assertions.assert(transaction.isFailure)
    Assertions.assert(transaction.failed.get.getMessage == "Value should be a number")
  }

  it should "calculate a credit transaction" in {
    val client = Client(1, "André", Balance(100, Limit(1000)))

    val transaction = Transaction.create("1000", "c", "desc", 1).get
    val balance = transaction.transactionType.newBalance(client)

    Assertions.assert(balance.total == 1100)
    Assertions.assert(balance.limit.value == 1000)
  }

  it should "calculate a debit transaction" in {
    val client = Client(1, "André", Balance(100, Limit(1000)))

    val transaction = Transaction.create("1000", "d", "desc", 1).get
    val balance = transaction.transactionType.newBalance(client)

    Assertions.assert(balance.total == -900)
    Assertions.assert(balance.limit.value == 1000)
  }
}
