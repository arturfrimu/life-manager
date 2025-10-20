create schema if not exists life_manager;

create table life_manager.exercise
(
    id          uuid primary key,
    name        varchar(128)             not null,
    type        varchar(64),
    description text,
    created     timestamp with time zone not null default now(),
    updated     timestamp with time zone not null default now(),
    version     int
);

create table life_manager.workout
(
    id          uuid primary key,
    name        varchar(128)             not null,
    description text,
    created     timestamp with time zone not null default now(),
    updated     timestamp with time zone not null default now(),
    version     int
);

create table life_manager.workout_exercise
(
    workout_id  uuid                     not null,
    exercise_id uuid                     not null,
    order_index int,
    created     timestamp with time zone not null default now(),
    updated     timestamp with time zone not null default now(),
    version     int,
    primary key (workout_id, exercise_id),
    constraint fk_workout_exercise_workout
        foreign key (workout_id)
            references life_manager.workout (id)
            on delete cascade,
    constraint fk_workout_exercise_exercise
        foreign key (exercise_id)
            references life_manager.exercise (id)
            on delete cascade
);

create table life_manager.repetition
(
    id          uuid primary key,
    weight      numeric(19, 0),
    completed   boolean                  not null,
    workout_id  uuid                     not null,
    exercise_id uuid                     not null,
    executed_at timestamp with time zone,
    created     timestamp with time zone not null default now(),
    updated     timestamp with time zone not null default now(),
    version     int,
    constraint fk_repetition_workout_exercise foreign key (workout_id, exercise_id)
        references life_manager.workout_exercise (workout_id, exercise_id)
);