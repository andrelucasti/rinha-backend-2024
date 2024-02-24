package io.andrelucas
package clients

case class Limit(value: Long):
  def until: Long = value * -1
