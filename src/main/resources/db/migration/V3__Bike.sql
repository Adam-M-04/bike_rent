CREATE TABLE brands (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE bike (
    id BIGSERIAL PRIMARY KEY,
    model VARCHAR(255) NOT NULL,
    brand_id BIGINT,
    color VARCHAR(255),
    available BOOLEAN NOT NULL DEFAULT true
);

ALTER TABLE bike
    ADD CONSTRAINT fk_bike_brand
        FOREIGN KEY (brand_id) REFERENCES brands(id);