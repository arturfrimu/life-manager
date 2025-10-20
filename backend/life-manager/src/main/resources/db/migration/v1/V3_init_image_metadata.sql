create table life_manager.image_metadata
(
    id           uuid primary key,
    exercise_id  uuid                     not null,
    object_key   varchar(128)             not null,
    content_type varchar(128)             not null,
    size_bytes   int                      not null,
    created      timestamp with time zone not null default now(),
    updated      timestamp with time zone not null default now(),
    version      int
);