create schema if not exists life_manager_history;

create table if not exists life_manager_history.revinfo
(
    rev
    bigserial
    primary
    key,
    revtstmp
    bigint
);

create table life_manager_history.exercise_history
(
    id            uuid                     not null,
    created       timestamp with time zone not null,
    updated       timestamp with time zone not null,
    name          varchar(128)             not null,
    type          varchar(64),
    description   text,
    revision_id   bigint                   not null,
    revision_type smallint                 not null,
    constraint fk_exercise_history_revinfo
        foreign key (revision_id)
            references life_manager_history.revinfo (rev)
            on delete cascade
);

create table life_manager_history.repetition_history
(
    id            uuid                     not null,
    created       timestamp with time zone not null,
    updated       timestamp with time zone not null,
    weight        numeric(19, 0),
    completed     boolean                  not null,
    executed_at   timestamp with time zone,
    revision_id   bigint                   not null,
    revision_type smallint                 not null,
    constraint fk_repetition_history_revinfo
        foreign key (revision_id)
            references life_manager_history.revinfo (rev)
            on delete cascade
);

create table life_manager_history.workout_history
(
    id            uuid                     not null,
    created       timestamp with time zone not null,
    updated       timestamp with time zone not null,
    name          varchar(128)             not null,
    description   text,
    revision_id   bigint                   not null,
    revision_type smallint                 not null,
    constraint fk_workout_history_revinfo
        foreign key (revision_id)
            references life_manager_history.revinfo (rev)
            on delete cascade
);

create table life_manager_history.workout_exercise_history
(
    workout_id    uuid                     not null,
    exercise_id   uuid                     not null,
    order_index   int,
    created       timestamp with time zone not null,
    updated       timestamp with time zone not null,
    revision_id   bigint                   not null,
    revision_type smallint                 not null,
    constraint fk_workout_exercise_history_revinfo
        foreign key (revision_id)
            references life_manager_history.revinfo (rev)
            on delete cascade
);