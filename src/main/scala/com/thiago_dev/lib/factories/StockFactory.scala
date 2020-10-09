package com.thiago_dev.lib.factories

import com.thiago_dev.stock.DTO.StockDTO
import com.thiago_dev.stock.entities.Stock
import com.thiago_dev.coffee.entities.Coffee

final case class StockFactory(stockDTO: StockDTO) 


object StockFactory {
    def apply (stockDTO: StockDTO): Stock = {
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
}