CREATE TABLE
    IF NOT EXISTS cards (
        id BIGSERIAL PRIMARY KEY,
        card_name VARCHAR(255) NOT NULL UNIQUE,
        account_id BIGSERIAL,
        card_number VARCHAR(16) NOT NULL UNIQUE,
        card_password VARCHAR(4) NOT NULL,
        cvv VARCHAR(3) NOT NULL UNIQUE,
        card_value DOUBLE PRECISION NOT NULL,
        expiration_date VARCHAR(10) NOT NULL,
        card_status VARCHAR(20) DEFAULT 'ACTIVE',
        FOREIGN KEY (account_id) REFERENCES account (id)
    );