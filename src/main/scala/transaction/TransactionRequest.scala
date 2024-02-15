package io.andrelucas
package transaction

import zio.json.{DeriveJsonEncoder, DeriveJsonDecoder, JsonDecoder, JsonEncoder}

case class TransactionRequest(valor: Int, tipo: String, descricao: String)

object TransactionRequest:
  given JsonEncoder[TransactionRequest] =
    DeriveJsonEncoder.gen[TransactionRequest]

  given JsonDecoder[TransactionRequest] =
    DeriveJsonDecoder.gen[TransactionRequest]
