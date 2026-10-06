CREATE TABLE districts (
    id UUID PRIMARY KEY,
    code VARCHAR(5) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,

    region_id UUID NOT NULL,

    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,

    CONSTRAINT fk_district_region
        FOREIGN KEY (region_id)
            REFERENCES regions(id)
            ON DELETE RESTRICT
);

CREATE INDEX idx_districts_region_id
    ON districts(region_id)