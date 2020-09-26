package com.thiago_dev.coffee.entities

import com.thiago_dev.coffee.DTO.CoffeeDTO
import scala.concurrent.ExecutionContext.Implicits.global
import com.thiago_dev.config.DoobieConfig.transactor
import doobie._
import implicits._

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

  def apply(id: Long, name: String, coffeeType: CoffeeEnum.CoffeeEnum, price: Double, cover_image_url: Option[String])
  : Coffee = new Coffee(id, name, coffeeType, price, cover_image_url)

  def save(coffee: Either[Coffee, CoffeeDTO]): Future[Int] = {
    if(coffee.left.get.isInstanceOf[Coffee]) createCoffee(coffee.left.get)
    else updateCoffee(coffee.right.get)
  }

  def findById(id: Long): Future[Option[Coffee]] = {
    sql"SELECT * FROM coffees WHERE id = $id"
      .query[Coffee]
      .option
      .transact(transactor)
      .unsafeToFuture()
  }

  def findAll(page: Int): Coffees = {
    sql"SELECT * FROM coffees LIMIT 20 OFFSET ($page * 20)"
      .query[Coffee]
      .to[List]
      .transact(transactor)
      .unsafeToFuture()
  }

  def createCoffee(coffee: Coffee): Future[Int] = {
    sql"""
    INSERT INTO coffees (name, "coffeeType", price, cover_image_url) VALUES (
         ${coffee.name},
         ${coffee.coffeeType}::CoffeeEnumType,
         ${coffee.price},
         ${coffee.cover_image_url}
    )"""
      .update.run.transact(transactor).unsafeToFuture()
  }

  def updateCoffee(coffeeDTO: CoffeeDTO): Future[Int] = {
    if(coffeeDTO.id.isEmpty) throw new Exception("Can't update item with no id")
    for {
      coffeeFromDB <- findById(coffeeDTO.id.get)
      updated <- {
        if(coffeeFromDB.isEmpty) throw new Exception("Couldn't find this object")

        var (id, name, coffeeType, price, cover_image_url) =
            Coffee.unapply(coffeeFromDB.get).get

        name = coffeeDTO.name.getOrElse(name)
        coffeeType = coffeeDTO.coffeeType.getOrElse(coffeeType)
        price = coffeeDTO.price.getOrElse(price)
        val image_url = coffeeDTO.cover_image_url.getOrElse(cover_image_url.get)

        sql"""
         UPDATE coffees
         SET
           "name" = $name,
           "coffeeType" = $coffeeType::CoffeeEnumType,
           "price" = $price,
           "cover_image_url" = $image_url
         WHERE coffees."id" = $id
        """.update.run.transact(transactor).unsafeToFuture()
      }
    } yield updated
  }
}