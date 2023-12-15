CREATE TABLE bank_account
(
    id      INTEGER        NOT NULL AUTO_INCREMENT,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    iban    VARCHAR(255),
    swift   VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE transactions
(
    id                 INTEGER        NOT NULL AUTO_INCREMENT,
    amount             DECIMAL(10, 2) NOT NULL,
    bank_account_id    INTEGER        NOT NULL,
    created_at         DATETIME(6)    NOT NULL,
    connexion          VARCHAR(255),
    description        VARCHAR(255)   NOT NULL,
    transaction_number VARCHAR(255)   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (bank_account_id) REFERENCES bank_account (id)
) ENGINE = InnoDB;

CREATE TABLE user
(
    id              INTEGER      NOT NULL AUTO_INCREMENT,
    user_id         INTEGER,
    email           VARCHAR(255) NOT NULL,
    firstname       VARCHAR(255) NOT NULL,
    lastname        VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    bank_account_id INTEGER UNIQUE,
    PRIMARY KEY (id),
    FOREIGN KEY (bank_account_id) REFERENCES bank_account (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE = InnoDB;

