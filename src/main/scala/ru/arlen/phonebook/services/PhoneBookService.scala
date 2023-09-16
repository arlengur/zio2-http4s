package ru.arlen.phonebook.services

import ru.arlen.phonebook.dao.entities.PhoneRecord
import ru.arlen.phonebook.dto.PhoneRecordDTO
import zio.{RIO, Task, ULayer, ZIO, ZLayer}
import zio.macros.accessible

import java.util.UUID

@accessible[PhoneBookService]
trait PhoneBookService {
  def find(phone: String): Task[PhoneRecordDTO]

  def insert(phoneRecord: PhoneRecordDTO): Task[String]

  def update(id: String, phoneRecord: PhoneRecordDTO): Task[Unit]

  def delete(id: String): Task[PhoneRecord]
}

class PhoneBookServiceImpl extends PhoneBookService {
  private val repo = scala.collection.mutable.Map[String, PhoneRecord]()

  override def find(phone: String): Task[PhoneRecordDTO] =
    ZIO.from(repo.find(_._2.phone == phone)
      .map(p => PhoneRecordDTO.from(p._2)))
      .orElseFail(new Throwable("Record not found"))

  override def insert(phoneRecord: PhoneRecordDTO): Task[String] = for {
    uuid <- ZIO.from(UUID.randomUUID().toString)
    _ <- ZIO.from(repo += uuid -> PhoneRecord(uuid, phoneRecord.phone, phoneRecord.fio))
  } yield uuid

  override def update(id: String, phoneRecord: PhoneRecordDTO): Task[Unit] = ZIO.from {
    val record = repo(id)
    repo.update(id, record.copy(phone = phoneRecord.phone, fio = phoneRecord.fio))
  }

  override def delete(id: String): Task[PhoneRecord] =
    ZIO.from(repo.remove(id)).orElseFail(new Throwable("Record not found"))
}

object PhoneBookService {
  val live: ULayer[PhoneBookServiceImpl] = ZLayer.succeed(new PhoneBookServiceImpl)

//  def find(phone: String): RIO[PhoneBookService, PhoneRecordDTO] =
//    ZIO.environmentWithZIO[PhoneBookService](_.get.find(phone))
//
//  def insert(phoneRecord: PhoneRecordDTO): RIO[PhoneBookService, String] =
//    ZIO.environmentWithZIO[PhoneBookService](_.get.insert(phoneRecord))
//
//  def update(id: String, phoneRecord: PhoneRecordDTO): RIO[PhoneBookService, Unit] =
//    ZIO.environmentWithZIO[PhoneBookService](_.get.update(id, phoneRecord))
//
//  def delete(id: String): RIO[PhoneBookService, PhoneRecord] =
//    ZIO.environmentWithZIO[PhoneBookService](_.get.delete(id))
}


