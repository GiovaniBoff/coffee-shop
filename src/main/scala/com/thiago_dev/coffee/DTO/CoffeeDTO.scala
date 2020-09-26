package com.thiago_dev.coffee.DTO

import com.thiago_dev.coffee.entities.CoffeeEnum.CoffeeEnum

case class CoffeeDTO(
  id: Option[Int],
  name: Option[String],
  coffeeType: Option[CoffeeEnum],
  price: Option[Double],
  cover_image_url: Option[String]
)
