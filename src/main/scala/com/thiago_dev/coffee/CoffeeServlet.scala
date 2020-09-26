package com.thiago_dev.coffee

import DTO.CoffeeDTO
import repositories.CoffeesRepository
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
    } yield Ok("data" -> coffees)
  }

  get("/:id") {
    val id = params("id").toInt
    for {
      coffee <- CoffeesRepository.findOne(id)
    } yield {
      if(coffee.isEmpty) NotFound(
        Message(s"The coffee with id `$id` was not found.")
      )
      else Ok(
        "data" -> coffee.get
      )
    }
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
