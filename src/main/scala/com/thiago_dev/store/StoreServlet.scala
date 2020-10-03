package com.thiago_dev.store

import org.scalatra._
import json.JacksonJsonSupport
import org.json4s.{DefaultFormats, Formats}
import scala.concurrent.ExecutionContext

import com.thiago_dev.lib.Utils.Message
import com.thiago_dev.stock.repositories.StockRepository.getStoreStock

class StoreServlet extends ScalatraServlet
  with JacksonJsonSupport
  with FutureSupport {

  override protected implicit def executor: ExecutionContext = ExecutionContext.global
  override protected implicit def jsonFormats: Formats = DefaultFormats

  before() { contentType = "application/json" }

  get("/") {
     // -
  }

  get("/:id") {

  }

  get("/stock/:storeId") {
    if(!params.contains("storeId")) {
      NotFound(
        Message("You should pass a valid storeId")
      )
    } else {
      val storeId = params.get("storeId").get.toLong
      for {
        stock <- getStoreStock(storeId)
      } yield "stock" -> stock
    }
  }
}
