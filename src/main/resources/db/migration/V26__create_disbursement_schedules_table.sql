CREATE TABLE disbursement_schedules (
    id UUID PRIMARY KEY,
    reference VARCHAR(10) NOT NULL UNIQUE,
    disbursement_month DATE NOT NULL,
    disbursement_amount NUMERIC(19, 2),
    percentage NUMERIC(3, 2),
    report_submission_date DATE NOT NULL,

    project_id UUID NOT NULL,

    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP
);

ALTER TABLE disbursement_schedules
    ADD CONSTRAINT fk_disbursement_schedules_project
        FOREIGN KEY (project_id)
            REFERENCES projects(id);

CREATE INDEX idx_disbursement_schedules_project_id
    ON disbursement_schedules(project_id);