TRUNCATE log_events CASCADE;

ALTER TABLE log_events ALTER COLUMN id RESTART WITH 1;

