CREATE TABLE bank_account
(
    id      int auto_increment,
    balance decimal(38, 2) not null,
    iban    varchar(255)   not null,
    swift   varchar(255)   not null,
    primary key (id)
);

CREATE TABLE transactions
(
    id                 int auto_increment,
    amount             decimal(38, 2) not null,
    connection         varchar(255)   not null,
    created_at         datetime(6)    not null,
    description        varchar(255)   null,
    transaction_number varchar(255)   not null,
    bank_account_id    int            not null,
    primary key (id),
    foreign key (bank_account_id) references bank_account (id)
);

CREATE TABLE user
(
    id              int auto_increment,
    email           varchar(255) not null,
    firstname       varchar(255) not null,
    lastname        varchar(255) not null,
    password        varchar(255) not null,
    role            varchar(255) not null,
    primary key (id),
    bank_account_id int          not null UNIQUE,
    foreign key (bank_account_id) references bank_account (id)
);

CREATE TABLE user_buddys
(
    buddys_id int not null,
    user_id   int not null,
    primary key (buddys_id, user_id),
    foreign key (buddys_id) references user (id),
    foreign key (user_id) references user (id)
);
