package com.thiago_dev.coffee

import DTO.CoffeeDTO
import com.thiago_dev.coffee.repositories.CoffeesRepository
import com.thiago_dev.lib.Utils.Message
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import json._

import scala.concurrent.ExecutionContext

class CoffeeServlet
  extends ScalatraServlet with JacksonJsonSupport with FutureSupport {

  override protected implicit def executor: ExecutionContext = ExecutionContext.global
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() { contentType = "application/json" }

  get("/") {
    for {
      coffees <- CoffeesRepository.findAll()
    } yield Ok("coffees" -> coffees)
  }

  post("/") {
    try {
      val coffeeDTO = parsedBody.extract[CoffeeDTO]

      for { saved <- CoffeesRepository.create(coffeeDTO) }
      yield Ok(
        Message(s"Coffee Created successfully! status: $saved")
      )
    } catch {
      case exception: Exception => UnprocessableEntity(Message(exception.getMessage))
    }
  }
}
