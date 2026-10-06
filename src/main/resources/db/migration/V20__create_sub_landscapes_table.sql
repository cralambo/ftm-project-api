CREATE TABLE sub_landscapes (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,

    landscape_id UUID NOT NULL,

    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,

    CONSTRAINT fk_sub_landscape_landscape
        FOREIGN KEY (landscape_id)
            REFERENCES landscapes(id)
            ON DELETE RESTRICT
);

CREATE INDEX idx_sub_landscape_landscape_id
    ON sub_landscapes(landscape_id)