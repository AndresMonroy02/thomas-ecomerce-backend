-- V1__Create_custom_user_table.sql
CREATE TABLE custom_user (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    city VARCHAR(255),
    role VARCHAR(255) NOT NULL
);