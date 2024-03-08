package io.andrelucas
package repository.client

import business.client.{Balance, Client, ClientRepository}

import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api.*

import java.time.LocalDateTime
import java.util.concurrent.Executors
import scala.concurrent.duration.*
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}

case class InDiskClientRepository(db: Database) extends ClientRepository {
  given ExecutionContext = ExecutionContext.fromExecutor(Executors.newVirtualThreadPerTaskExecutor())
  override def save(client: Client): Unit = ???

  override def findById(clientId: Long): Option[Client] =
    val action = clientTable
      .filter(_.id === clientId).result
      .transactionally

    val future = db.run(action)

    val option = Await.result(future, 2.second)

    option.headOption.map(
      table => ClientEntity(
        table.id,
        table.name,
        table.limit,
        table.balance,
        table.updatedAt
      ).toClient
    )
}
