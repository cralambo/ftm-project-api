CREATE TABLE landscapes (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP
);