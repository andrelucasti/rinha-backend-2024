package io.andrelucas
package transaction

import org.json4s.*
import org.json4s.native.JsonMethods.*
import org.json4s.native.Serialization.{read, write}

case class TransactionRequest(valor: Int,
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