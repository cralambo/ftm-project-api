CREATE TABLE positions (
    id UUID PRIMARY KEY,
    code VARCHAR(10),
    name VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP
);