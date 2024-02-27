DROP TABLE IF EXISTS courses;

CREATE TABLE urls (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    created_at timestamp
);