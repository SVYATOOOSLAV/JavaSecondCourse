--liquibase formatted sql
--changeset Milkin Svyatoslav:1
create table users_links(
    id_user bigint not null Primary key,
    id_link bigint not null,
    FOREIGN KEY (id_user) REFERENCES users (id),
    FOREIGN KEY (id_link) REFERENCES links (id)
);
