package io.andrelucas
package business.client

case class Limit(value: Long):
  def until: Long = value * -1
