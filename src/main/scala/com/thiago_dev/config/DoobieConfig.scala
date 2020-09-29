package com.thiago_dev.config

import cats.effect.{ContextShift, IO}
import doobie.Transactor

import scala.concurrent.ExecutionContext

object DoobieConfig {
  implicit val executor: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val transactor:  Transactor.Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver",
    url    = "jdbc:postgresql://localhost:5432/coffee_shop",
    user   = "postgres",
    pass   = "123"
  )
}
