CREATE TABLE report_status_histories (
    id UUID PRIMARY KEY,

    partner_report_id UUID NOT NULL,

    previous_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,

    action_by UUID NOT NULL,
    comment TEXT,

    action_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_report_history_report
        FOREIGN KEY (partner_report_id)
            REFERENCES partner_reports(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_report_history_user
        FOREIGN KEY (action_by)
            REFERENCES users(id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_history_previous_status
        CHECK (
            previous_status IS NULL
                OR previous_status IN (
                    'DRAFT',
                    'SUBMITTED',
                    'UNDER_REVIEW',
                    'RETURNED',
                    'IN_VALIDATION',
                    'APPROVED',
                    'REJECTED'
                )
        ),

    CONSTRAINT chk_history_new_status
        CHECK (new_status IN (
            'DRAFT',
            'SUBMITTED',
            'UNDER_REVIEW',
            'RETURNED',
            'IN_VALIDATION',
            'APPROVED',
            'REJECTED'
        ))
);

CREATE INDEX idx_report_history_report_date
    ON report_status_histories(
                partner_report_id,
                action_at DESC
        );

CREATE INDEX idx_report_history_action_by
    ON report_status_histories(action_by);
