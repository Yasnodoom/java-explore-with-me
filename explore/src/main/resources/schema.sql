CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat FLOAT,
    lot FLOAT,
    UNIQUE (lat, lot)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    title VARCHAR(120) NOT NULL,
    state VARCHAR(10) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE CHECK (event_date > CURRENT_TIMESTAMP + interval '2 hours'),
    published TIMESTAMP WITHOUT TIME ZONE,
    paid BOOLEAN,
    request_moderation BOOLEAN,
    category_id BIGINT REFERENCES categories (id),
    initiator_id BIGINT REFERENCES users (user_id),
    locations_id BIGINT REFERENCES locations (id),
    participant_limit INTEGER
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id BIGINT REFERENCES events (id),
    requester_id BIGINT REFERENCES users (user_id),
    status VARCHAR(10) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    pinned BOOLEAN,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS events_compilations (
    event_id BIGINT REFERENCES events (id),
    compilations_id BIGINT REFERENCES compilations (id),
    PRIMARY KEY (event_id,  compilations_id)
);

