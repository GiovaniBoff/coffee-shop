package com.thiago_dev.stock.DTO

import com.thiago_dev.coffee.entities.CoffeeEnum.CoffeeEnum

case class StockDTO(
  store_id: Long,
  coffee_id: Long,
  price: Double,
  coffeeType: CoffeeEnum,
  name: String,
  cover_image_url: String,
  coffee_quantity: Int
)
