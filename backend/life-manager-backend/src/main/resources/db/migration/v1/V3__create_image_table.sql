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

create table life_manager.exercise_images
(
    exercise_id uuid not null,
    image_id    uuid not null,
    constraint pk_exercise_images primary key (exercise_id, image_id),
    constraint fk_exercise_images_exercise foreign key (exercise_id) references life_manager.exercise (id),
    constraint fk_exercise_images_image foreign key (image_id) references life_manager.image (id)
);

create table life_manager_history.exercise_images_history
(
    exercise_id uuid not null,
    image_id    uuid not null,
    rev         integer not null,
    revtype     smallint,
    constraint pk_exercise_images_history primary key (exercise_id, image_id, rev),
    constraint fk_exercise_images_hist_rev foreign key (rev) references life_manager_history.revinfo (rev)
);