package io.andrelucas
package transaction

enum TransactionType(value: String) {
  case CREDIT extends TransactionType("c")
  case DEBIT extends  TransactionType("d")

}