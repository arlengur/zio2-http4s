package ru.arlen.phonebook

import zio.{ZIO, _}

object Main extends ZIOAppDefault {
  override def run: ZIO[Any, Any, Any] = Server.server.provide(Server.appEnv).exitCode
}