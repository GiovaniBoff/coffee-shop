package com.thiago_dev.stock.repositories

import com.thiago_dev.stock.DTO.StockRowDTO
import com.thiago_dev.stock.entities.Stock, Stock.StockObject

import scala.concurrent.Future

object StockRepository {
  def findOne(stockId: Long): Future[Option[StockRowDTO]] =
    Stock.findById(stockId)

  def save: StockRowDTO => Future[Int] =
    Stock.save

  def getStoreStock(storeId: Int): Future[StockObject] = Stock.getStoreStock(storeId)
}
