DROP TABLE IF EXISTS courses;

CREATE TABLE urls (
    id BIGINT PRIMARY KEY GENERATED BY DEFAULT  AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);