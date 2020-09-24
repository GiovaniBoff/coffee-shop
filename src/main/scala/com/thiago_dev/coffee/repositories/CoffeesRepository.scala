package com.thiago_dev.coffee.repositories

import com.thiago_dev.coffee.entities.Coffee
import scala.concurrent.Future

object CoffeesRepository {
  def findAll(): Coffee.Coffees = Coffee.findAll()

  def findOne(id: Int): Future[Option[Coffee]] = Coffee.findById(id)

  def create = Coffee.save _
}
