package io.andrelucas
package statement

import org.json4s.*
import org.json4s.native.JsonMethods.*
import org.json4s.native.Serialization.{read, write}

import java.time.{LocalDate, LocalDateTime}


case class StatementResponse(saldo: BalanceResponse, ultimas_transacoes: List[StatementTransactionResponse])

object StatementResponse:
  given formats : DefaultFormats.type = DefaultFormats

  def fromJson(json: String): StatementResponse =
    read[StatementResponse](json)

  extension (sr: StatementResponse)
    def toJson: String = write(sr)



case class BalanceResponse(total: Long, limite: Long, data_extrato: String)
object BalanceResponse:
  given formats: DefaultFormats.type = DefaultFormats

case class StatementTransactionResponse(valor: Long, tipo: String, descricao: String, realizada_em: String)
object StatementTransactionResponse:
  given formats : DefaultFormats.type = DefaultFormats