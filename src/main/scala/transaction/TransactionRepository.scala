package io.andrelucas
package transaction

import clients.{Balance, Limit}

import zio.{Task, ZIO}

trait TransactionRepository {
  def save(transaction: Transaction): Task[(Limit, Balance)]
}

object TransactionRepository {
  def save(transaction: Transaction): ZIO[TransactionRepository, Throwable, (Limit, Balance)] =
    ZIO.serviceWithZIO[TransactionRepository](_.save(transaction))
}