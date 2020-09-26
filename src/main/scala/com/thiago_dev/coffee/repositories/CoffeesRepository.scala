package com.thiago_dev.coffee.repositories

import com.thiago_dev.coffee.DTO.CoffeeDTO
import com.thiago_dev.coffee.entities.Coffee

import scala.concurrent.Future

object CoffeesRepository {
  def getPaged(page: Int): Future[List[Coffee]] = Coffee.findAll(page)

  def findOne(id: Int): Future[Option[Coffee]] = Coffee.findById(id)

  def save: Either[Coffee, CoffeeDTO] => Future[Int] = Coffee.save
}
