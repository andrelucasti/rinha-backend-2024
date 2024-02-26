package io.andrelucas
package transaction

import org.json4s.*
import org.json4s.native.JsonMethods.*
import org.json4s.native.Serialization.{read, write}

case class TransactionResponse(limite: Long, saldo: Long)

object TransactionResponse {
  def fromJson(json:String): TransactionResponse =
    read[TransactionResponse](json)

  extension(tr: TransactionResponse)
    def toJson: String = write(tr)
  given formats: DefaultFormats.type = DefaultFormats
}