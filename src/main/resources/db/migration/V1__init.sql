create table if not exists todo
(
    id           bigint identity primary key,
    name         varchar(100) not null,
    completed    bool         not null,
    completed_at timestamp,
    created_at   timestamp    not null default current_timestamp(),
    updated_at   timestamp    not null default current_timestamp()
);