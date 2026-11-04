-- Remplacer uniquement les types non reconnus par OTHER.
-- Les valeurs NULL restent NULL.
UPDATE report_attachments
SET document_type = 'OTHER'
WHERE document_type IS NOT NULL
  AND document_type NOT IN (
                            'TECHNICAL_SUPPORT',
                            'FINANCIAL_SUPPORT',
                            'OFFICIAL_LETTER',
                            'CONTRACT',
                            'REQUEST_FOR_NEXT_DISBURSEMENT',
                            'OTHER'
    );

-- Vérifier que les valeurs non NULL correspondent à l'Enum.
ALTER TABLE report_attachments
    ADD CONSTRAINT chk_report_attachment_document_type
        CHECK (
            document_type IS NULL
                OR document_type IN (
                                     'TECHNICAL_SUPPORT',
                                     'FINANCIAL_SUPPORT',
                                     'OFFICIAL_LETTER',
                                     'CONTRACT',
                                     'REQUEST_FOR_NEXT_DISBURSEMENT',
                                     'OTHER'
                )
            );