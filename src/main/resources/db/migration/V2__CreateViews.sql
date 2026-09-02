CREATE TABLE IF NOT EXISTS client_representation(
    id UUID PRIMARY KEY,
    first_name VARCHAR(150) NOT NULL,
    last_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS product_representation(
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    unity_value NUMERIC(19,2) NOT NULL,
    supplier VARCHAR(150) NOT NULL,
    product_type VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS product_stock_representation(
    id UUID PRIMARY KEY,
    stock INTEGER NOT NULL,
    reserved INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS order_representation(
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL,
    total NUMERIC(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    reason VARCHAR(255),
    payment_method VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS order_item_representation(
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    order_id UUID NOT NULL,
    quantity INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_representation(
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item_representation(
    id UUID PRIMARY KEY,
    cart_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL
);
