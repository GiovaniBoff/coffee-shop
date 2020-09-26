CREATE TYPE CoffeeEnumType AS ENUM (
  'black',
  'latte',
  'with milk',
  'cappuccino',
  'americano',
  'espresso',
  'doppio',
  'cortado',
  'red eye',
  'galão',
  'lungo'
);

CREATE TABLE "coffees" (
  "id" SERIAL PRIMARY KEY,
  "name" VARCHAR(80),
  "coffeeType" CoffeeEnumType,
  "price" NUMERIC
);

CREATE TABLE "stores" (
  "id" SERIAL PRIMARY KEY,
  "name" VARCHAR(255),
  "popularity" SMALLINT
);

CREATE TABLE "stock" (
  "id" SERIAL PRIMARY KEY,
  "coffee_id" INT,
  "store_id" INT,
  "quantity" INT
);

ALTER TABLE "stock" ADD FOREIGN KEY ("store_id") REFERENCES "stores" ("id");

ALTER TABLE "stock" ADD FOREIGN KEY ("coffee_id") REFERENCES "coffees" ("id");

CREATE INDEX "coffee_index" ON "coffees" ("name")
