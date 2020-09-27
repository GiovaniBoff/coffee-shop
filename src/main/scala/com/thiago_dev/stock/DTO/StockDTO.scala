package com.thiago_dev.stock.DTO

import com.thiago_dev.coffee.entities.CoffeeEnum.CoffeeEnum

case class StockDTO(
  store_index: Long,
  quantity: Int,
  id: Long,
  name: String,
  coffeeType: CoffeeEnum,
  price: Double,
  cover_image_url: String
)
