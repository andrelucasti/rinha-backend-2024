package io.andrelucas
package business.transaction

case class LimitException(private var msg: String) extends Throwable(msg)
  
