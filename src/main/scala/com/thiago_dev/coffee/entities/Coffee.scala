package com.thiago_dev.coffee.entities

import com.thiago_dev.coffee.DTO.CoffeeDTO
import com.thiago_dev.config.DoobieConfig.transactor
import doobie._, implicits._

import scala.concurrent.Future

case class Coffee(
  id: Long,
  name: String,
  coffeeType: CoffeeEnum.CoffeeEnum = CoffeeEnum.BLACK,
  price: Double,
  cover_image_url: Option[String]
)

object Coffee {
  type Coffees = Future[List[Coffee]]

  implicit def convertFromDTO(coffeeDTO: CoffeeDTO): Coffee =
      Coffee(
          0,
          coffeeDTO.name.get,
          coffeeDTO.coffeeType.getOrElse(CoffeeEnum.BLACK),
          coffeeDTO.price.get,
          coffeeDTO.cover_image_url
      )

  def apply(id: Long, name: String,
            coffeeType: CoffeeEnum.CoffeeEnum, price: Double, cover_image_url: Option[String]): Coffee = {
    new Coffee(id, name, coffeeType, price, cover_image_url)
  }

  def save(coffee: Coffee): Future[Int] = {
    val (id, name, coffeeType, price, cover_image_url) = Coffee.unapply(coffee).get
    sql"""
    INSERT INTO coffees (name, "coffeeType", price, cover_image_url) VALUES (
         $name,
         $coffeeType::CoffeeType,
         $price,
         $cover_image_url
    )"""
    .update.run.transact(transactor).unsafeToFuture()
  }

  def findById(id: Int): Future[Option[Coffee]] = {
    sql"SELECT * FROM coffees WHERE id = $id"
      .query[Coffee]
      .option
      .transact(transactor)
      .unsafeToFuture()
  }

  def findAll(): Coffees = {
    sql"SELECT * FROM coffees"
      .query[Coffee]
      .to[List]
      .transact(transactor)
      .unsafeToFuture()
  }
}