package com.thiago_dev.stock

import com.thiago_dev.lib.Utils.Message
import com.thiago_dev.stock.repositories.StockRepository
import org.scalatra._
import json.JacksonJsonSupport
import org.json4s.{DefaultFormats, Formats}

import scala.concurrent.ExecutionContext

class StockServlet extends ScalatraServlet
  with JacksonJsonSupport
  with FutureSupport {
  case class Quantity( quantity: Int )

  override protected implicit def executor: ExecutionContext = ExecutionContext.global
  override protected implicit def jsonFormats: Formats = DefaultFormats

  before() { contentType = "application/json" }

  get("/") {
    if(!params.contains("storeId")) {
      NotFound(
        Message("You should pass a valid storeId")
      )
    } else {
      val storeId = params.get("storeId").get.toLong
        for {
        stock <- StockRepository.getStoreStock(storeId)
      } yield "stock" -> stock
    }
  }

  post("/") {

  }

  patch("/stockQuantity") {
    if(!params.contains("stockId")) {
      NotFound(
        Message("You should pass a valid stockId")
      )
    } else {
      val stockId = params("stockId").toLong
      val requestBody = parsedBody.extract[Quantity]

      StockRepository.update(stockId, requestBody.quantity)
    }
  }
}
