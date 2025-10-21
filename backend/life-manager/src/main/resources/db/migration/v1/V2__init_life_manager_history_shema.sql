create schema if not exists life_manager_history;

create table if not exists life_manager_history.revinfo
(
    rev bigserial primary key,
    revtstmp bigint,
    username varchar(128),
    source_ip varchar(128)
);

create table life_manager_history.user_history
(
    id            uuid                     not null,
    username      varchar(128)             not null,
    email         varchar(256)             not null,
    created       timestamp with time zone not null,
    updated       timestamp with time zone not null,
    revision_id   bigint                   not null,
    revision_type smallint                 not null,
    constraint fk_user_history_revinfo
        foreign key (revision_id)
            references life_manager_history.revinfo (rev)
            on delete cascade
);

create table life_manager_history.exercise_history
(
    id                  uuid                     not null,
    name                varchar(128)             not null,
    type                varchar(64),
    description         text,
    created_by_user_id  uuid,
    created             timestamp with time zone not null,
    updated             timestamp with time zone not null,
    revision_id         bigint                   not null,
    revision_type       smallint                 not null,
    constraint fk_exercise_history_revinfo
        foreign key (revision_id)
            references life_manager_history.revinfo (rev)
            on delete cascade
);

create table life_manager_history.workout_session_history
(
    id            uuid                     not null,
    user_id       uuid                     not null,
    name          varchar(128),
    notes         text,
    started_at    timestamp with time zone,
    completed_at  timestamp with time zone,
    created       timestamp with time zone not null,
    updated       timestamp with time zone not null,
    revision_id   bigint                   not null,
    revision_type smallint                 not null,
    constraint fk_workout_session_history_revinfo
        foreign key (revision_id)
            references life_manager_history.revinfo (rev)
            on delete cascade
);

create table life_manager_history.workout_exercise_history
(
    id                  uuid                     not null,
    workout_session_id  uuid                     not null,
    exercise_id         uuid                     not null,
    order_index         int                      not null,
    notes               text,
    created             timestamp with time zone not null,
    updated             timestamp with time zone not null,
    revision_id         bigint                   not null,
    revision_type       smallint                 not null,
    constraint fk_workout_exercise_history_revinfo
        foreign key (revision_id)
            references life_manager_history.revinfo (rev)
            on delete cascade
);

create table life_manager_history.set_history
(
    id                   uuid                     not null,
    workout_exercise_id  uuid                     not null,
    set_index            int                      not null,
    reps                 int,
    weight               numeric(10, 2),
    completed            boolean                  not null,
    notes                text,
    created              timestamp with time zone not null,
    updated              timestamp with time zone not null,
    revision_id          bigint                   not null,
    revision_type        smallint                 not null,
    constraint fk_set_history_revinfo
        foreign key (revision_id)
            references life_manager_history.revinfo (rev)
            on delete cascade
);