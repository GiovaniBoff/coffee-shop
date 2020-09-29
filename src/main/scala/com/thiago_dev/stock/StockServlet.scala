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

  override protected implicit def executor: ExecutionContext = ExecutionContext.global
  override protected implicit def jsonFormats: Formats = DefaultFormats

  before() { contentType = "application/json" }

  get("/") {
    val storeId = if(params.contains("storeId")) params.get("storeId").get.toInt else 0

    if(storeId == 0) {
      NotFound(Message(
        """You should pass a valid storeId
          |for viewing this data""".stripMargin
      ))
    } else {
      for {
        stock <- StockRepository.getStoreStock(storeId)
      } yield "stock" -> stock
    }
  }

  post("/") {

  }
}
