package io.andrelucas
package unit

import clients.{Balance, Limit}
import transaction.{LimitException, Transaction, TransactionType}

import org.scalatest.Assertions
import org.scalatest.flatspec.AnyFlatSpec

class TransactionTest extends AnyFlatSpec {
  it should "return a transaction of type credit" in {
    val transaction = Transaction.create(1000, "c", "desc", 1).get

    Assertions.assert(transaction.transactionType.prefix() == "c")
    Assertions.assert(transaction.value == 1000)
  }
  it should "return a transaction of type debit" in {
    val transaction = Transaction.create(2000, "d", "desc", 1).get

    Assertions.assert(transaction.transactionType.prefix() == "d")
    Assertions.assert(transaction.value == 2000)
  }

  it should "throw exception when balance is less than limit" in {

  }
}
