--liquibase formatted sql
--changeset Milkin Svyatoslav:1
create table links(
    id bigint generated always as identity Primary key,
    url TEXT not null,
    updated_at timestamp with time zone not null
);
