package com.thiago_dev.search

import com.thiago_dev.search.DTO.SearchDTO
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.{FutureSupport, ScalatraServlet}
import org.scalatra.json.JacksonJsonSupport

import scala.concurrent.ExecutionContext

class SearchServlet
    extends ScalatraServlet
      with JacksonJsonSupport
      with FutureSupport {

  override protected implicit def executor: ExecutionContext = ExecutionContext.global
  override protected implicit def jsonFormats: Formats = DefaultFormats

  before() { contentType = "application/json" }

  post("/") {
    val searchQuery = parsedBody.extract[SearchDTO]
  }
}
