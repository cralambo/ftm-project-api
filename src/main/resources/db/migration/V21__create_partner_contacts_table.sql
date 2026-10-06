CREATE TABLE partner_contacts (
    id UUID PRIMARY KEY,
    firstname VARCHAR(150) NOT NULL,
    lastname VARCHAR(150),
    email VARCHAR(150),
    phone VARCHAR(20) NOT NULL,
    address TEXT,
    user_id UUID NOT NULL,
    support_partner_id UUID NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,

    CONSTRAINT uk_partner_contacts_user_id
        UNIQUE (user_id),


    CONSTRAINT fk_partner_contact_user
        FOREIGN KEY (user_id)
            REFERENCES users(id),

    CONSTRAINT fk_partner_contact_support_partner
        FOREIGN KEY (support_partner_id)
            REFERENCES support_partners(id)
            ON DELETE RESTRICT
);

CREATE INDEX idx_partner_contacts_support_partner_id
    ON partner_contacts(support_partner_id);