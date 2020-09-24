package com.thiago_dev.shop

import org.scalatra._
import views.html.{ hello }

class ShopServlet extends ScalatraServlet {

  get("/") {
    Ok(hello("Online Coffee Shop"))
  }
}
