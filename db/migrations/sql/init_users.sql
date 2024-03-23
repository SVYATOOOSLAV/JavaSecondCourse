--liquibase formatted sql
--changeset Milkin Svyatoslav:1
create table users(
    id bigint Primary key,
    first_name TEXT not null
);
