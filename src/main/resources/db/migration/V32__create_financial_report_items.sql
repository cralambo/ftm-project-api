CREATE TABLE financial_report_items (
    id UUID PRIMARY KEY,

    partner_report_id UUID NOT NULL,

    designation VARCHAR(255) NOT NULL,
    quantity NUMERIC(19, 2) NOT NULL,
    unit_amount NUMERIC(19, 2) NOT NULL,

    total_amount NUMERIC(19, 2)
        GENERATED ALWAYS AS (quantity * unit_amount) STORED,

    observations TEXT,

    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,

    CONSTRAINT fk_financial_item_report
        FOREIGN KEY (partner_report_id)
            REFERENCES partner_reports(id)
            ON DELETE CASCADE,

    CONSTRAINT chk_financial_quantity
        CHECK (quantity > 0),

    CONSTRAINT chk_financial_unit_amount
        CHECK (unit_amount >= 0)
);

CREATE INDEX idx_financial_items_report
    ON financial_report_items(partner_report_id);
