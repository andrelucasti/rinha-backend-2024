package io.andrelucas
package app.transaction

import org.json4s.*
import org.json4s.native.JsonMethods.*
import org.json4s.native.Serialization.{read, write}

case class TransactionRequest(valor: String,
                              tipo: String,
                              descricao: String)

object TransactionRequest {
  def fromJson(json:String): TransactionRequest =
    read[TransactionRequest](json)

  //implicit val
  given formats : DefaultFormats.type = DefaultFormats
  extension (tr: TransactionRequest)
    def toJson: String = 
      write(tr)
}
