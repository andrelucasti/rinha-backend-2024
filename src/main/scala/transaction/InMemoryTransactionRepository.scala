package io.andrelucas
package transaction

import clients.{Balance, Limit}

import zio.*

import java.util.UUID
import scala.collection.mutable.ListBuffer

case class InMemoryTransactionRepository(buffer:ListBuffer[Transaction]) extends TransactionRepository:

  override def save(transaction: Transaction): Task[Transaction] =
    buffer += transaction
    ZIO.succeed(transaction)
    
    

object InMemoryTransactionRepository {
  def layer: ZLayer[Any, Nothing, InMemoryTransactionRepository] =
    ZLayer.fromZIO(
      //TODO Maybe must use Ref.make
      ZIO.succeed(new InMemoryTransactionRepository(ListBuffer.empty))
    )
}


