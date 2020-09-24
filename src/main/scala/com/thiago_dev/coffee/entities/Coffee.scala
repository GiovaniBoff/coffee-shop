package com.thiago_dev.coffee.entities

import com.thiago_dev.coffee.DTO.CoffeeDTO
import com.thiago_dev.config.DoobieConfig.transactor
import doobie._, implicits._

import scala.concurrent.Future

case class Coffee(
  id: Long,
  name: String,
  coffeeType: CoffeeEnum.CoffeeEnum = CoffeeEnum.BLACK,
  price: Double
)

object Coffee {
  type Coffees = Future[List[Coffee]];

  def apply(id: Long, name: String, coffeeType: CoffeeEnum.CoffeeEnum, price: Double): Coffee = {
    new Coffee(id, name, coffeeType, price)
  }

  def validAsCoffee(coffee: CoffeeDTO): Boolean = {
    if(coffee.coffeeType.isEmpty) false;
    else if(coffee.name.isEmpty) false;
    else if(coffee.price.isEmpty) false;
    else true;
  }

  implicit def convertFromDTO(coffeeDTO: CoffeeDTO): Coffee = {
    if(validAsCoffee(coffeeDTO))
      Coffee(
        0,
        coffeeDTO.name.get,
        coffeeDTO.coffeeType.get,
        coffeeDTO.price.get
      )
    else throw new Exception(s"Can't convert from ${coffeeDTO.getClass.getTypeName} to Coffee")
  }

  def save(coffee: Coffee): Future[Int] = {
    val (id, name, coffeeType, price) = Coffee.unapply(coffee).get;
    sql"""INSERT INTO coffees (name, "coffeeType", price) VALUES ($name, $coffeeType::CoffeeType, $price)"""
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