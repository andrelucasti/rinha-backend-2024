package io.andrelucas
package transaction

case class LimitException(private var msg: String) extends Throwable(msg)
  
