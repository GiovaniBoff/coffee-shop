package com.thiago_dev.store.entities

case class Store (
   id: BigInt,
   name: String,
   popularity: Int,
   cover_image_url: String
)

object Store {
  def apply(id: BigInt, name: String, popularity: Int, cover_image_url: String): Store =
      new Store(id, name, popularity, cover_image_url)
}
