package ru.arlen.phonebook.endpoint

import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes}
import ru.arlen.phonebook.dto.PhoneRecordDTO
import ru.arlen.phonebook.services.PhoneBookService
import zio.{RIO, ZIO}
import zio.interop.catz._

class PhoneBookEndPoint[R <: PhoneBookService] {
  type PhoneBookTask[A] = RIO[R, A]
  private val dsl = Http4sDsl[PhoneBookTask]

  import dsl._

  implicit def jsonDecoder[A](implicit decoder: Decoder[A]): EntityDecoder[PhoneBookTask, A] = jsonOf[PhoneBookTask, A]

  implicit def jsonEncoder[A](implicit encoder: Encoder[A]): EntityEncoder[PhoneBookTask, A] = jsonEncoderOf[PhoneBookTask, A]

  val routes: HttpRoutes[PhoneBookTask] = HttpRoutes.of[PhoneBookTask] {

    case GET -> Root / phone => PhoneBookService.find(phone).foldZIO(
      err =>
        {ZIO.logInfo("Start")
        NotFound()},
      result => Ok(result)
    )
    case req@POST -> Root => {
      for {
        record <- req.as[PhoneRecordDTO]
        result <- PhoneBookService.insert(record)
      } yield result
    }.foldZIO(
      err => NotFound(err.getMessage),
      result => Ok(result)
    )
    case req@PUT -> Root / id => {
      for {
        record <- req.as[PhoneRecordDTO]
        result <- PhoneBookService.update(id, record)
      } yield result
    }.foldZIO(
      err => NotFound(),
      result => Ok(result)
    )
    case req@DELETE -> Root / id => PhoneBookService.delete(id).foldZIO(
      err => NotFound(),
      result => Ok(result)
    )
  }
}
