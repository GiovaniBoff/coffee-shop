package com.thiago_dev.stock.repositories

import com.thiago_dev.stock.DTO.{StockRowDTO, UpdateStockDTO}
import com.thiago_dev.stock.entities.Stock
import Stock.StockObject

import scala.concurrent.Future

object StockRepository {
  def findOne: Long => Future[Option[StockRowDTO]] = Stock.findById

  def save: StockRowDTO => Future[Int] = Stock.create

  def getStoreStock: Long => Future[StockObject] = Stock.getStoreStock

  def update(stockId: Long, quantity: Int, increment: Boolean = true): Future[UpdateStockDTO] =
    Stock.updateStockQuantity(stockId, quantity, increment)
}
