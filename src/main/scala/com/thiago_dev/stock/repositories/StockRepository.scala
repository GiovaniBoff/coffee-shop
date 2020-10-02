package com.thiago_dev.stock.repositories

import com.thiago_dev.stock.DTO.StockRowDTO
import com.thiago_dev.stock.entities.Stock, Stock.StockObject

import scala.concurrent.Future

object StockRepository {
  def findOne: Long => Future[Option[StockRowDTO]] = Stock.findById

  def save: StockRowDTO => Future[Int] = Stock.create

  def getStoreStock: Long => Future[StockObject] = Stock.getStoreStock

  def update(stockId: Long, quantity: Int, increment: Boolean = true): Future[Int] =
    Stock.updateStockQuantity(stockId, quantity, increment)
}
