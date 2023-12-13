SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE user;
TRUNCATE TABLE bank_account;
TRUNCATE TABLE transactions;
SET FOREIGN_KEY_CHECKS =1;

INSERT INTO user (email, password, firstname, lastname)
VALUES ("test@gmail.com", "passtest", "firstname", "lastname"),
       ("test2@gmail.com", "passtest2", "firstname2", "lastname2");

INSERT INTO bank_account (balance, iban, swift)
VALUES (2563.87, "7d90428b-d6f2-4f9e-8587-7dd2d8bef932", "97bd3f45-c0d4-4498-987a-3816353a9fc0"),
       (563.87, "6f6dc57a-dfba-4230-bce0-23dbb01bd78e", "1f8ab0d0-03d1-4d8b-8889-89693bbd7d18");

INSERT INTO transactions (amount, connexion, created_At, description, transaction_number, bank_account_id)
VALUES (80.00, "test2@gmail.com", "2023-12-12 10:14:37.482103", "the first test pay", "1a08adce-d8u3-4778-b200-336661ece172", 1),
       (250.50, "test@gmail.com", "2023-10-12 10:15:48.385974", "the second test pay", "2i01adce-d8u9-4778-b200-336981ece123", 2);