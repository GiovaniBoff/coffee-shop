package com.thiago_dev.store.entities

import com.thiago_dev.config.DoobieConfig.transactor

import scala.concurrent.ExecutionContext.Implicits.global
import doobie._
import implicits._

import scala.concurrent.Future

case class Store (
  id: BigInt,
  name: String,
  popularity: Int,
  cover_image_url: String
)

object Store {
  def getOne(id: Long): Future[Option[Store]] = {
    sql"SELECT * FROM stores WHERE id = $id"
      .query[Store].option.transact(transactor).unsafeToFuture()
  }

  def paged(page: Int): Unit = {}

  def create(store: Store): Unit = {}

  def update(store: Store): Unit = {}
}
