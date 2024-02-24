package io.andrelucas
package unit

import clients.{Balance, Limit}
import transaction.TransactionType.{CREDIT, DEBIT}
import transaction.{LimitException, Transaction, TransactionType}

import org.scalatest.Assertions
import org.scalatest.flatspec.AnyFlatSpec

class TransactionTest extends AnyFlatSpec {
  it should "sum the balance when a transaction type is credit" in {
    val transaction = Transaction.create(Balance(1000, Limit(10000)), 1000, CREDIT, "creditTransaction", 1L).get

    Assertions.assert(transaction.transactionType == CREDIT)
    Assertions.assert(transaction.value == 2000)
  }
  it should "subtract the client balance when a transaction type is debit" in {
    val transaction = Transaction.create(Balance(3000, Limit(10000)), 1000, DEBIT, "debitTransaction", 1L).get

    Assertions.assert(transaction.transactionType == DEBIT)
    Assertions.assert(transaction.value == 2000)
  }

  it should "throw exception when balance is less than limit" in {
    Assertions.assertThrows[LimitException]{
      Transaction.create(Balance(1000, Limit(1000)), 2001, DEBIT, "debitTransaction", 1L).get
    }
  }
}
