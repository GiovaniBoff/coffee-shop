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
      store_id = stockDTO.store_id,
      coffee = Coffee(
        price = stockDTO.price,
        id = stockDTO.coffee_id,
        name = stockDTO.name,
        coffeeType = stockDTO.coffeeType,
        cover_image_url = Some(stockDTO.cover_image_url)
      ),
      quantity = stockDTO.coffee_quantity
    )
  }

  implicit def convertFromDTO(stockList: StockList): StockObject = {
    stockList.map(stockFactory)
  }

  private def loadStock(storeId: Long): Future[StockList] = {
    sql"SELECT * FROM coffeestock WHERE store_id = $storeId"
      .query[StockDTO].to[List].transact(transactor).unsafeToFuture()
  }

  def getStoreStock(storeId: Long): Future[StockObject] = {
    for {
      storeList <- loadStock(storeId)
    } yield storeList
  }

  def create(stockRowDTO: StockRowDTO): Future[Int] = {
    val (id, store_id, coffee_id, quantity) = StockRowDTO.unapply(stockRowDTO).get
    sql"""
      INSERT INTO stock (store_id, coffee_id, quantity)
      VALUES ($store_id, $coffee_id, $quantity)
    """.update.run.transact(transactor).unsafeToFuture()
  }

  def findById(stockId: Long): Future[Option[StockRowDTO]] = {
    sql"SELECT * FROM stock WHERE id = $stockId"
      .query[StockRowDTO].option.transact(transactor).unsafeToFuture()
  }

  def updateStockQuantity(stockId: Long, quantity: Int, increment: Boolean = true): Future[Int] = {
    for {
      stock <- findById(stockId)
      updated <- {
        if(stock.isEmpty) {
          new Exception("Cannot get this stock")
        }
        val stockQuantity = stock.get.quantity

        val newQuantity = if(increment) {
          stockQuantity + quantity
        } else {
          if(stockQuantity > quantity) {
            stockQuantity - quantity
          }
          else {
            quantity % stockQuantity
          }
        }

        sql"""
          UPDATE stock SET quantity = $newQuantity
          WHERE stock.id = $stockId
        """.update.run.transact(transactor).unsafeToFuture()

        Future(newQuantity)
      }
    } yield updated
  }
}