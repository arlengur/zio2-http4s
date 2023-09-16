package ru.arlen.phonebook

import com.comcast.ip4s.{Host, Port}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import ru.arlen.phonebook.endpoint.PhoneBookEndPoint
import ru.arlen.phonebook.services.{PhoneBookService, PhoneBookServiceImpl}
import zio.interop.catz._
import zio.{RIO, ULayer, ZIO}

object Server {
  type AppEnv = PhoneBookService
  val appEnv: ULayer[PhoneBookServiceImpl] = PhoneBookService.live

  type AppTask[A] = RIO[AppEnv, A]

  private val httpApp = Router("/api" -> new PhoneBookEndPoint[AppEnv]().routes).orNotFound

  val server: ZIO[AppEnv, Serializable, Unit] = for {
    host <- ZIO.from(Host.fromString("0.0.0.0"))
    port <- ZIO.from(Port.fromInt(8080))
    _ <- EmberServerBuilder.default[AppTask]
      .withHost(host)
      .withPort(port)
      .withHttpApp(httpApp)
      .build
      .useForever
  } yield ()
}
