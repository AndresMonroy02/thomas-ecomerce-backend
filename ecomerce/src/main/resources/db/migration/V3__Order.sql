CREATE TABLE orders (
    id SERIAL PRIMARY KEY, 
    id_user BIGINT,
    start_date DATE,
    end_date DATE,
    total_amount DECIMAL(19, 2),
    state VARCHAR(255),
    FOREIGN KEY (id_user) REFERENCES custom_user(id) 
);


CREATE TABLE order_item (
    id SERIAL PRIMARY KEY,  
    order_id BIGINT,
    product_id BIGINT,
    quantity INT,
    moment_price DECIMAL(19, 2),
    price_pay DECIMAL(19, 2),
    discount DOUBLE PRECISION,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES product(id) 
);


CREATE TABLE discounts (
    id SERIAL PRIMARY KEY,  
    start_data DATE,
    end_date DATE,
    value DOUBLE PRECISION,
    active BOOLEAN
);
