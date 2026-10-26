CREATE TABLE partner_reports (
    id UUID PRIMARY KEY,

    disbursement_schedule_id UUID NOT NULL,
    partner_contact_id UUID NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',

    submitted_at TIMESTAMP,
    assigned_to UUID,
    assigned_at TIMESTAMP,

    general_comments TEXT,
    rejection_reason TEXT,

    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,

    CONSTRAINT fk_report_schedule
        FOREIGN KEY (disbursement_schedule_id)
            REFERENCES disbursement_schedules(id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_report_partner_contact
        FOREIGN KEY (partner_contact_id)
            REFERENCES partner_contacts(id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_report_assigned_user
        FOREIGN KEY (assigned_to)
            REFERENCES users(id)
            ON DELETE RESTRICT,

    CONSTRAINT uq_report_schedule
        UNIQUE (disbursement_schedule_id),

    CONSTRAINT chk_partner_report_status
        CHECK (status IN (
            'DRAFT',
            'SUBMITTED',
            'UNDER_REVIEW',
            'RETURNED',
            'IN_VALIDATION',
            'APPROVED',
            'REJECTED'
        ))
);

CREATE INDEX idx_partner_reports_contact
    ON partner_reports(partner_contact_id);

CREATE INDEX idx_partner_reports_status
    ON partner_reports(status);

CREATE INDEX idx_partner_reports_assigned_to
    ON partner_reports(assigned_to);

CREATE INDEX idx_partner_reports_schedule_status
    ON partner_reports(disbursement_schedule_id, status);