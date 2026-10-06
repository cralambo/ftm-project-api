CREATE TABLE municipalities (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,

    district_id UUID NOT NULL,

    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,

    CONSTRAINT fk_municipality_district
        FOREIGN KEY (district_id)
            REFERENCES districts(id)
            ON DELETE RESTRICT
);

CREATE INDEX idx_municipalities_district_id ON municipalities(district_id)