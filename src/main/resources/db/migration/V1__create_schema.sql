CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(19, 2) NOT NULL,
    sku VARCHAR(64) NOT NULL UNIQUE,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE shopping_carts (
    id BIGSERIAL PRIMARY KEY,
    customer_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(64) NOT NULL UNIQUE,
    status VARCHAR(32) NOT NULL,
    total_amount NUMERIC(19, 2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE line_items (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(19, 2) NOT NULL,
    shopping_cart_id BIGINT REFERENCES shopping_carts(id),
    order_id BIGINT REFERENCES orders(id),
    CONSTRAINT line_item_owner_check CHECK (
        (shopping_cart_id IS NOT NULL AND order_id IS NULL) OR
        (shopping_cart_id IS NULL AND order_id IS NOT NULL)
    )
);

CREATE TABLE checkouts (
    id BIGSERIAL PRIMARY KEY,
    shopping_cart_id BIGINT NOT NULL REFERENCES shopping_carts(id),
    order_id BIGINT REFERENCES orders(id),
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE TABLE shipping (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE REFERENCES orders(id),
    recipient_name VARCHAR(255) NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(128) NOT NULL,
    postal_code VARCHAR(32) NOT NULL,
    country VARCHAR(2) NOT NULL,
    carrier VARCHAR(128),
    tracking_number VARCHAR(128),
    status VARCHAR(32) NOT NULL
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE REFERENCES orders(id),
    checkout_id BIGINT NOT NULL UNIQUE REFERENCES checkouts(id),
    amount NUMERIC(19, 2) NOT NULL,
    method VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    transaction_id VARCHAR(128),
    paid_at TIMESTAMP
);
