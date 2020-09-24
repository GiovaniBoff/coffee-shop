package com.thiago_dev

import com.thiago_dev.shop.ShopServlet
import org.scalatra.test.ClientResponse
import org.scalatra.test.scalatest._

class CoffeeShopTests extends ScalatraFunSuite {

  addServlet(classOf[ShopServlet], "/*")

  test("GET / on ShopServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

  override def header = ???
}
