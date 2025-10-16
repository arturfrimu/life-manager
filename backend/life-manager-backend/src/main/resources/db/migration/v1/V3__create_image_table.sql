create table life_manager.image
(
    id           uuid primary key,
    file_name    varchar(255)             not null,
    object_key   varchar(512)             not null,
    bucket_name  varchar(255)             not null,
    url          varchar(1024)            not null,
    content_type varchar(128)             not null,
    size         bigint                   not null,
    created      timestamp with time zone not null default now(),
    updated      timestamp with time zone not null default now(),
    version      int
);


create table life_manager_history.image_history
(
    id           uuid primary key,
    file_name    varchar(255)             not null,
    object_key   varchar(512)             not null,
    bucket_name  varchar(255)             not null,
    url          varchar(1024)            not null,
    content_type varchar(128)             not null,
    size         bigint                   not null,
    created      timestamp with time zone not null default now(),
    updated      timestamp with time zone not null default now(),
    revision_id   bigint                   not null,
    revision_type smallint                 not null,
    constraint fk_image_history_revinfo
        foreign key (revision_id)
            references life_manager_history.revinfo (rev)
            on delete cascade
);