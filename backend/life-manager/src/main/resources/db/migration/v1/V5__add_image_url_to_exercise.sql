alter table life_manager.exercise
    add column if not exists image_url varchar(2048);

alter table life_manager_history.exercise_history
    add column if not exists image_url varchar(2048);