CREATE OR REPLACE VIEW public.coffeestock
 AS
   SELECT str.id AS store_index,
    stk.quantity,
    coffee.id,
    coffee.name,
    coffee."coffeeType",
    coffee.price,
    coffee.cover_image_url
   FROM stores str
     LEFT JOIN stock stk ON str.id = stk.store_id
     LEFT JOIN coffees coffee ON stk.coffee_id = coffee.id
   WHERE stk.quantity >= 0;
