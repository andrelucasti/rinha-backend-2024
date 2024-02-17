package io.andrelucas
package transaction

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class TransactionResponse(limit: Long, balance: Long)

object TransactionResponse:
  given JsonEncoder[TransactionResponse] =
    DeriveJsonEncoder.gen[TransactionResponse]

  given JsonDecoder[TransactionResponse] =
    DeriveJsonDecoder.gen[TransactionResponse]
