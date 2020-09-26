CREATE OR REPLACE VIEW coffeeStock AS
    SELECT
		str.id AS store_index,
		stk.quantity,
		coffee.name,
		coffee."coffeeType",
		coffee.price
	FROM stores AS str
    LEFT JOIN stock AS stk ON str.id = stk.store_id
	LEFT JOIN coffees AS coffee ON stk.coffee_id = coffee.id