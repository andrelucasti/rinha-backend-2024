package io.andrelucas
package business.transaction

case class LimitException(private var msg: String) extends Throwable(msg)
case class RequiredException(private var msg: String) extends Throwable(msg)
case class TenCharactersException(private var msg: String) extends Throwable(msg)
  
