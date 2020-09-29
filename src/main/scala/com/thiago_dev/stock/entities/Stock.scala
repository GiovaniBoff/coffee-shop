package com.thiago_dev.stock.entities

import com.thiago_dev.coffee.entities.Coffee

import scala.concurrent.ExecutionContext.Implicits.global
import com.thiago_dev.config.DoobieConfig.transactor
import com.thiago_dev.stock.DTO.{StockDTO, StockRowDTO}
import doobie._
import implicits._

import scala.concurrent.Future

case class Stock(
  store_id: Long,
  coffee: Coffee,
  quantity: Int
)

object Stock {
  type StockObject = List[Stock]
  type StockList = List[StockDTO]

  private def stockFactory(stockDTO: StockDTO): Stock = {
    Stock(
      stockDTO.store_id,
      Coffee(
        stockDTO.coffee_id,
        stockDTO.name,
        stockDTO.coffeeType,
        stockDTO.price,
        Some(stockDTO.cover_image_url)
      ),
      stockDTO.coffee_quantity
    )
  }

  implicit private def convertFromDTO(stockList: StockList): StockObject = {
    stockList.map(stockFactory)
  }

  private def loadStock(storeId: Long): Future[StockList] = {
    sql"SELECT * FROM coffeestock WHERE store_id = $storeId"
      .query[StockDTO].to[List].transact(transactor)
      .unsafeToFuture()
  }

  def getStoreStock(storeId: Long): Future[StockObject] = {
    for {
      stockList <- loadStock(storeId)
    } yield stockList
  }

  def save(stockRowDTO: StockRowDTO): Future[Int] = {
    if(stockRowDTO.id.isEmpty) createStock(stockRowDTO)
    else updateStockQuantity(stockRowDTO)
  }

  private def createStock(stockRowDTO: StockRowDTO): Future[Int] = {
    val (id, store_id, coffee_id, quantity) = StockRowDTO.unapply(stockRowDTO).get
    sql"""
       INSERT INTO stock (store_id, coffee_id, quantity)
       VALUES ($store_id, $coffee_id, $quantity)
     """.update.run.transact(transactor).unsafeToFuture()
  }

  private def updateStockQuantity(stockRowDTO: StockRowDTO): Future[Int] = {
    val (id, store_id, coffee_id, quantity) = StockRowDTO.unapply(stockRowDTO).get
    sql"""
       UPDATE stock
       SET
        quantity = $quantity
       WHERE stock.id = $id
     """.update.run.transact(transactor).unsafeToFuture()
  }

  def findById(stockId: Long): Future[Option[StockRowDTO]] = {
    sql"SELECT * FROM stock WHERE id = $stockId"
      .query[StockRowDTO]
      .option
      .transact(transactor)
      .unsafeToFuture()
  }

  def incrementQuantity(stockId: Long, incrementBy: Int): Future[Int] = {
    for {
      stock <- findById(stockId)
    } yield {
      if(stock.isEmpty) throw new Exception("Cannot find this stock")
      val stockDTO = stock.get

      val incremented = stockDTO.quantity + incrementBy

      save(stockDTO.copy( quantity = incremented ))

      incremented
    }
  }

  def decrementQuantity(stockId: Long, decrementBy: Int): Future[Int] = {
    for {
      stock <- findById(stockId)
    } yield {
      if(stock.isEmpty) throw new Exception("Cannot find this stock")
      val stockDTO = stock.get

      val decremented = if(stockDTO.quantity >= decrementBy) {
        stockDTO.quantity - decrementBy
      } else {
        stockDTO.quantity % decrementBy
      }

      save(stockDTO.copy( quantity = decremented ))

      return Future {
        decremented
      }
    }
  }
}