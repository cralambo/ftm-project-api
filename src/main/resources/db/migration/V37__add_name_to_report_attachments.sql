ALTER TABLE report_attachments
    ADD COLUMN name VARCHAR(150);

UPDATE report_attachments
SET name = original_filename
WHERE name IS NULL;

ALTER TABLE report_attachments
    ALTER COLUMN name SET NOT NULL;