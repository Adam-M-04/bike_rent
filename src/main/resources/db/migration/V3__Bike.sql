CREATE TABLE bike (
    id SERIAL PRIMARY KEY,
    model VARCHAR(255) NOT NULL,
    brand_id BIGINT,
    color VARCHAR(255),
    available BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE brands (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

ALTER TABLE bike
    ADD CONSTRAINT fk_bike_brand
        FOREIGN KEY (brand_id) REFERENCES brands(id);