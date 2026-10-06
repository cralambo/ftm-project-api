CREATE TABLE board_members (
    id UUID PRIMARY KEY,
    firstname VARCHAR(150) NOT NULL,
    lastname VARCHAR(150),
    personal_email VARCHAR(150),
    professional_email VARCHAR(150),
    phone VARCHAR(20) NOT NULL,
    address TEXT,
    position VARCHAR(150) NOT NULL,
    appointment_date DATE NOT NULL,
    end_date DATE,
    user_id UUID NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,

    CONSTRAINT uk_board_members_user_id
        UNIQUE (user_id),

    CONSTRAINT fk_board_members_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
);