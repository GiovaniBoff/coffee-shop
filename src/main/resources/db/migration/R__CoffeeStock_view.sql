CREATE OR REPLACE VIEW public.coffeestock
AS
  SELECT
    store_id,
    coffee_id,
    cfs.price,
    cfs."coffeeType",
    cfs.name,
    cfs.cover_image_url,
    SUM(quantity) AS coffee_quantity
  FROM public.stock
  LEFT JOIN coffees AS cfs ON cfs.id = coffee_id
  WHERE quantity > 0
  GROUP BY
    stock.store_id,
    coffee_id,
    cfs."coffeeType",
    cfs.cover_image_url,
    cfs.price,
    cfs.name;