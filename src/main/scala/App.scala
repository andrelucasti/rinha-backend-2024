package io.andrelucas

import clients.InMemoryClientRepository

object App {

  @main
  def main(): Unit = {
    AppConfiguration(InMemoryClientRepository()).start(7070)
  }
}
