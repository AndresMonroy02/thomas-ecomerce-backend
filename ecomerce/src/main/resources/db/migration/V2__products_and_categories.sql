CREATE TABLE product (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price DOUBLE PRECISION,
  img VARCHAR(255),
  state BOOLEAN NOT NULL
);

CREATE TABLE category (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE product_category (
  product_id INTEGER REFERENCES product(id),
  category_id INTEGER REFERENCES category(id),
  PRIMARY KEY (product_id, category_id)
);