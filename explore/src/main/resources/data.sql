TRUNCATE requests, events, users, locations, categories, compilations, events_compilations CASCADE;

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;
ALTER TABLE locations ALTER COLUMN id RESTART WITH 1;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 1;
ALTER TABLE events ALTER COLUMN id RESTART WITH 1;
ALTER TABLE requests ALTER COLUMN id RESTART WITH 1;
ALTER TABLE compilations ALTER COLUMN id RESTART WITH 1;
