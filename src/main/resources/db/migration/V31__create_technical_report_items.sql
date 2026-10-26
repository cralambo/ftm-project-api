CREATE TABLE technical_report_items (
    id UUID PRIMARY KEY,

    partner_report_id UUID NOT NULL,
    disbursement_activity_id UUID NOT NULL,

    achieved_quantity NUMERIC(19, 2),
    achieved_unit VARCHAR(50),
    result_description TEXT,
    observations TEXT,

    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,

    CONSTRAINT fk_technical_item_report
        FOREIGN KEY (partner_report_id)
            REFERENCES partner_reports(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_technical_item_activity
        FOREIGN KEY (disbursement_activity_id)
            REFERENCES disbursement_activities(id)
            ON DELETE RESTRICT,

    CONSTRAINT uq_technical_report_activity
        UNIQUE (partner_report_id, disbursement_activity_id),

        CONSTRAINT chk_achieved_quantity
            CHECK (
                achieved_quantity IS NULL
                    OR achieved_quantity >= 0
            )
);

CREATE INDEX idx_technical_items_report
    ON technical_report_items(partner_report_id);

CREATE INDEX idx_technical_items_activity
    ON technical_report_items(disbursement_activity_id);
