package com.thiago_dev.config

import cats.effect.{ContextShift, IO}
import doobie.Transactor

import scala.concurrent.ExecutionContext

object DoobieConfig {
  implicit val executor: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  lazy val db = Map(
    "url"       -> sys.env.getOrElse("DB_URL_JDBC", "postgresql://localhost:5432/coffee_shop"),
    "user"      -> sys.env.getOrElse("DATABASE_USER", "postgres"),
    "password"  -> sys.env.getOrElse("DATABASE_PASSWORD", "123")
  )

  lazy val transactor:  Transactor.Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver",
    url    = s"jdbc:${db("url")}",
    user   = db("user"),
    pass   = db("password")
  )
}
