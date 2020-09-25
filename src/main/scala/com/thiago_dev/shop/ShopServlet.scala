package com.thiago_dev.shop

import org.scalatra._
import views.html.{ hello, store }

class ShopServlet extends ScalatraServlet {

  get("/") {
    Ok(
      hello("Online Coffee Shop")
    )
  }

  get("/store") {
    Ok(
      store("Starbucks", "http://via.placeholder.com/850x300")
    )
  }
}
