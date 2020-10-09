package com.thiago_dev.stock

import org.scalatra._
import json.JacksonJsonSupport
import org.json4s.{DefaultFormats, Formats}
import scala.concurrent.ExecutionContext

import com.thiago_dev.stock.DTO.{QuantityDTO, StockRowDTO}
import com.thiago_dev.lib.Utils.Message
import com.thiago_dev.stock.repositories.StockRepository

class StockServlet extends ScalatraServlet
  with JacksonJsonSupport with FutureSupport {

  override protected implicit def executor: ExecutionContext = ExecutionContext.global
  override protected implicit def jsonFormats: Formats = DefaultFormats

  before() { contentType = "application/json" }

  get("/:id") {
    if(!params.contains("id")) {
      NotFound(
        Message("Couldn't find this stock id")
      )
    } else {
      val id = params("id").toLong
      StockRepository.findOne(id)
    }
  }

  post("/") {
    val stockBody: Option[StockRowDTO] = parsedBody.extractOpt[StockRowDTO]

    if(stockBody.isDefined) {
      StockRepository.save(stockBody.get)
    } else {
      UnprocessableEntity(
        Message("You should pass a valid stock object")
      )
    }
  }

  patch("/quantity") {
    if(!params.contains("stockId")) {
      NotFound(
        Message("You should pass a valid stockId")
      )
    } else {
      val stockId = params("stockId").toLong
      val quantityDTO = parsedBody.extractOpt[QuantityDTO]
    
      if(quantityDTO.isDefined) {
        val (quantity, increment) = QuantityDTO.unapply(quantityDTO.get).get

        StockRepository.update(stockId, quantity, increment)
      } else {
        UnprocessableEntity(
          Message("You should pass a valid quantity value!")
        )
      }
    }
  }

  error {
    case e: Exception =>
      InternalServerError(
        Message(s"This wasn't supposed to occur... Please, try again later.\n ${e.getMessage}")
      )
  }
}
