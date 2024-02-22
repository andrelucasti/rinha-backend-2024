package io.andrelucas
package transaction

enum TransactionType(value: String) {
  case CREDIT extends TransactionType("c")
  case DEBIT extends TransactionType("d")
}

object TransactionType {
   def fromString(value: String): TransactionType = {
     value.toLowerCase match
       case "c" => TransactionType.CREDIT
       case "d" => TransactionType.DEBIT
   }
}