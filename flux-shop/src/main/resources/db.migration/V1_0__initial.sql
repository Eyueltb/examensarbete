create table if not exists _product
(
    id   char(36) not null primary key,
    name varchar(255),
    description   varchar(255),
    price decimal,
    version bigint
);
