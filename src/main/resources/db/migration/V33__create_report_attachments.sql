CREATE TABLE report_attachments (
    id UUID PRIMARY KEY,

    partner_report_id UUID NOT NULL,
    technical_report_item_id UUID,
    financial_report_item_id UUID,

    original_filename VARCHAR(255) NOT NULL,
    storage_key TEXT NOT NULL UNIQUE,
    content_type VARCHAR(150),
    file_size BIGINT NOT NULL,

    document_type VARCHAR(50),
    description TEXT,

    uploaded_by UUID NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_attachment_report
        FOREIGN KEY (partner_report_id)
            REFERENCES partner_reports(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_attachment_technical_item
        FOREIGN KEY (technical_report_item_id)
            REFERENCES technical_report_items(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_attachment_financial_item
        FOREIGN KEY (financial_report_item_id)
            REFERENCES financial_report_items(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_attachment_uploaded_by
        FOREIGN KEY (uploaded_by)
            REFERENCES users(id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_attachment_size
        CHECK (file_size > 0),

    CONSTRAINT chk_attachment_target
        CHECK (
            NOT (
                technical_report_item_id IS NOT NULL
                AND financial_report_item_id IS NOT NULL
            )
        )
);

CREATE INDEX idx_attachments_report
    ON report_attachments(partner_report_id);

CREATE INDEX idx_attachments_technical_item
    ON report_attachments(technical_report_item_id);

CREATE INDEX idx_attachments_financial_item
    ON report_attachments(financial_report_item_id);

CREATE INDEX idx_attachments_uploaded_by
    ON report_attachments(uploaded_by);
