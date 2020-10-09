package com.thiago_dev.stock.entities

import com.thiago_dev.coffee.entities.Coffee
import com.thiago_dev.config.DoobieConfig.transactor
import com.thiago_dev.stock.DTO.{StockDTO, StockRowDTO, UpdateStockDTO}

import doobie._, implicits._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.thiago_dev.lib.factories.StockFactory

case class Stock(
  store_id: Long,
  coffee: Coffee,
  quantity: Int
)

object Stock {
  type StockList = List[Stock]
  type StockDTOList = List[StockDTO]

  implicit def convertFromDTO(stockList: StockDTOList): StockList = {
    stockList.map(StockFactory(_))
  }

  private def loadStock(storeId: Long): Future[StockDTOList] = {
    sql"SELECT * FROM coffeestock WHERE store_id = $storeId"
      .query[StockDTO].to[List].transact(transactor).unsafeToFuture()
  }

  def getStoreStock(storeId: Long): Future[StockList] = {
    for {
      storeList <- loadStock(storeId)
    } yield storeList
  }

  def create(stockRowDTO: StockRowDTO): Future[Int] = {
    val (_, store_id, coffee_id, quantity) = StockRowDTO.unapply(stockRowDTO).get
    sql"""
      INSERT INTO stock (store_id, coffee_id, quantity)
      VALUES ($store_id, $coffee_id, $quantity)
    """.update.run.transact(transactor).unsafeToFuture()
  }

  def findById(stockId: Long): Future[Option[StockRowDTO]] = {
    sql"SELECT * FROM stock WHERE id = $stockId"
      .query[StockRowDTO].option.transact(transactor).unsafeToFuture()
  }

  def updateStockQuantity(stockId: Long, quantity: Int, increment: Boolean = true): Future[UpdateStockDTO] = {
    for {
      
      stock <- findById(stockId)
      
      remainsOnStock <- {
        if(stock.isEmpty) {
          throw new Exception("Cannot get this stock")
        }

        val stockQuantity: Int = stock.get.quantity

        if(increment) {
          Future(stockQuantity + quantity)
        } else {
          if(stockQuantity > quantity) {
            Future(stockQuantity - quantity)
          } else {
            throw new Exception(s"Cannot update this object, stock quantity is under expected: $stockQuantity")
          }
        }
      }
      
      sqlUpdate <- {
        sql"""
          UPDATE stock SET quantity = $remainsOnStock
          WHERE stock.id = $stockId
        """.update.run.transact(transactor).unsafeToFuture()
      }
      
      response <- {
        Future(UpdateStockDTO("Updated successfully", remainsOnStock))
      }
    } yield response
  }
}