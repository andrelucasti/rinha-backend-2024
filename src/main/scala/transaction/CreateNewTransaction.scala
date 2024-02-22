package io.andrelucas
package transaction

import clients.Balance

object CreateNewTransaction:
  def run(currentClientBalance: Balance, newTransaction: Transaction): Balance =
    Balance(currentClientBalance.value + newTransaction.value)