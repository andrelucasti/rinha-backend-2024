package io.andrelucas
package transaction

import clients.{Balance, Limit}

import zio.{Task, ZIO}

trait TransactionRepository {
  def save(transaction: Transaction):ZIO[Any, Throwable, Transaction]
}

object TransactionRepository {
  def save(transaction: Transaction): ZIO[TransactionRepository, Throwable, Transaction] =
    ZIO.serviceWithZIO[TransactionRepository](_.save(transaction))
}