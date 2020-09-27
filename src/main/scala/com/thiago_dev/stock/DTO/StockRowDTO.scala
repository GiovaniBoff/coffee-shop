package com.thiago_dev.stock.DTO

case class StockRowDTO(
  id: Option[Long],
  store_id: Long,
  coffee_id: Long,
  quantity: Int
)
