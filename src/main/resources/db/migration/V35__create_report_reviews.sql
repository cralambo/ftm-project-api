CREATE TABLE report_reviews (
    id UUID PRIMARY KEY,

    partner_report_id UUID NOT NULL,
    reviewer_id UUID NOT NULL,

    review_type VARCHAR(30) NOT NULL,
    decision VARCHAR(30) NOT NULL,

    comments TEXT NOT NULL,
    reviewed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_report_review_report
        FOREIGN KEY (partner_report_id)
            REFERENCES partner_reports(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_report_review_user
        FOREIGN KEY (reviewer_id)
            REFERENCES users(id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_review_type
        CHECK (review_type IN (
            'INITIAL_CHECK',
            'TECHNICAL',
            'FINANCIAL',
            'FINAL_VALIDATION'
        )),

    CONSTRAINT chk_review_decision
        CHECK (decision IN (
            'ACCEPTED',
            'RETURNED',
            'REJECTED'
        ))
);

CREATE INDEX idx_report_reviews_report_date
    ON report_reviews(
                      partner_report_id,
                      reviewed_at DESC
        );

CREATE INDEX idx_report_reviews_reviewer
    ON report_reviews(reviewer_id);
