package com.thiago_dev.coffee

import DTO.CoffeeDTO
import com.thiago_dev.coffee.entities.Coffee
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
    val page = if(params.contains("page")) params("page").toInt else 0
    for {
      coffees <- CoffeesRepository.getPaged(page)
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
      val coffeeDTO = parsedBody.extract[Coffee]

      //val coffee = Coffee(0, coffeeDTO.name.get)

      for { saved <- CoffeesRepository.save(Left(coffeeDTO)) }
      yield Ok(
        Message(s"$saved Coffee Created successfully!")
      )
    } catch {
      case exception: Exception => UnprocessableEntity(Message(exception.getMessage))
    }
  }

  patch("/") {
    val coffee = parsedBody.extract[CoffeeDTO]

    for {
      updated <- CoffeesRepository.save(Right(coffee))
    } yield updated
  }
}
