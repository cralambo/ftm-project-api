CREATE TABLE IF NOT EXISTS disbursement_activities (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    disbursement_schedule_id UUID NOT NULL,
    description TEXT,
    type VARCHAR(20) NOT NULL,

    --- QUANTITATIVE
    target_quantity NUMERIC(19,2),
    unit VARCHAR(50),

    --- QUALITATIVE
    success_criteria TEXT,

    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,

    CONSTRAINT fk_activity_disbursement_schedule
        FOREIGN KEY (disbursement_schedule_id)
        REFERENCES disbursement_schedules(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_activity_type
        CHECK (type IN ('QUANTITATIVE', 'QUALITATIVE')),

    CONSTRAINT chk_activity_type_fields CHECK (
        (type = 'QUANTITATIVE'
            AND target_quantity IS NOT NULL
            AND unit IS NOT NULL
            AND success_criteria IS NULL)
        OR
        (type = 'QUALITATIVE'
            AND success_criteria IS NOT NULL
            AND target_quantity IS NULL
            AND unit IS NULL
        )
    )
);

CREATE INDEX idx_activity_disbursement_schedules
    ON disbursement_activities(disbursement_schedule_id);