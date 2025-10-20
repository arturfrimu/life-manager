create schema if not exists life_manager;

create table if not exists life_manager.user
(
    id       uuid primary key,
    username varchar(128)             not null unique,
    email    varchar(256)             not null unique,
    created  timestamp with time zone not null,
    updated  timestamp with time zone not null,
    version  int                      not null default 0
);

create table if not exists life_manager.exercise
(
    id                  uuid primary key,
    name                varchar(128)             not null,
    type                varchar(64),
    description         text,
    created_by_user_id  uuid,
    created             timestamp with time zone not null,
    updated             timestamp with time zone not null,
    version             int                      not null default 0,
    constraint fk_exercise_created_by_user foreign key (created_by_user_id) references life_manager.user (id)
);

create table if not exists life_manager.workout_session
(
    id           uuid primary key,
    user_id      uuid                     not null,
    name         varchar(128),
    notes        text,
    started_at   timestamp with time zone,
    completed_at timestamp with time zone,
    created      timestamp with time zone not null,
    updated      timestamp with time zone not null,
    version      int                      not null default 0,
    constraint fk_workout_session_user foreign key (user_id) references life_manager.user (id)
);

create table if not exists life_manager.workout_exercise
(
    id                  uuid primary key,
    workout_session_id  uuid                     not null,
    exercise_id         uuid                     not null,
    order_index         int                      not null,
    notes               text,
    created             timestamp with time zone not null,
    updated             timestamp with time zone not null,
    version             int                      not null default 0,
    constraint fk_workout_exercise_session foreign key (workout_session_id) references life_manager.workout_session (id) on delete cascade,
    constraint fk_workout_exercise_exercise foreign key (exercise_id) references life_manager.exercise (id),
    constraint uq_workout_exercise_session_order unique (workout_session_id, order_index)
);

create table if not exists life_manager.set
(
    id                   uuid primary key,
    workout_exercise_id  uuid                     not null,
    set_index            int                      not null,
    reps                 int,
    weight               numeric(10, 2),
    duration_seconds     int,
    distance_meters      numeric(10, 2),
    completed            boolean                  not null,
    notes                text,
    created              timestamp with time zone not null,
    updated              timestamp with time zone not null,
    version              int                      not null default 0,
    constraint fk_set_workout_exercise foreign key (workout_exercise_id) references life_manager.workout_exercise (id) on delete cascade,
    constraint uq_set_workout_exercise_index unique (workout_exercise_id, set_index)
);