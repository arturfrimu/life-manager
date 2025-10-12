create schema if not exists life_manager;
create schema if not exists life_manager_history;
create extension if not exists "uuid-ossp";
set search_path to life_manager,life_manager_history,public;