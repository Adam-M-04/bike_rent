CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    bike_id BIGINT NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_reservation_bike FOREIGN KEY (bike_id) REFERENCES bike(id)
);

