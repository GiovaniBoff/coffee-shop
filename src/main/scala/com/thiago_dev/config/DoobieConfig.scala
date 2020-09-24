package com.thiago_dev.config

import cats.effect.IO
import doobie.Transactor
import scala.concurrent.ExecutionContext

object DoobieConfig {
  implicit val executor = IO.contextShift(ExecutionContext.global)

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/coffee_shop",
    "postgres",
    "123"
  )
}
