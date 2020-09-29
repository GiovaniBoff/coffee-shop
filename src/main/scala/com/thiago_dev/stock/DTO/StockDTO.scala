package com.thiago_dev.stock.DTO

import com.thiago_dev.coffee.entities.CoffeeEnum.CoffeeEnum

case class StockDTO(
  store_id: Long,
  coffee_quantity: Int,
  coffee_id: Long,
  name: String,
  coffeeType: CoffeeEnum,
  cover_image_url: String,
  price: Double
)
