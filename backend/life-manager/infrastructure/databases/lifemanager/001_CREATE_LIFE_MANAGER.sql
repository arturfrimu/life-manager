DO
$$
BEGIN
    IF
NOT EXISTS (SELECT FROM pg_database WHERE datname = 'life_manager') THEN
       CREATE
DATABASE life_manager;
END IF;
END $$;