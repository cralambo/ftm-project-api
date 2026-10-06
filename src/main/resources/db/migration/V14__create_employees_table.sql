CREATE TABLE employees (
    id UUID PRIMARY KEY,
    employee_number VARCHAR(10) NOT NULL UNIQUE,
    firstname VARCHAR(150) NOT NULL,
    lastname VARCHAR(150),
    acronym VARCHAR(10) NOT NULL UNIQUE,
    personal_email VARCHAR(150),
    professional_email VARCHAR(150),
    phone VARCHAR(20) NOT NULL,
    address TEXT,
    hire_date DATE NOT NULL,
    user_id UUID NOT NULL,
    position_id UUID NOT NULL,
    department_id UUID NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,

    CONSTRAINT uk_employees_user_id
        UNIQUE (user_id),

    CONSTRAINT uk_employees_employee_number
        UNIQUE (employee_number),

    CONSTRAINT uk_employees_acronym
        UNIQUE (acronym),

    CONSTRAINT fk_employees_user
        FOREIGN KEY (user_id)
            REFERENCES users(id),

    CONSTRAINT fk_employee_position
        FOREIGN KEY (position_id)
            REFERENCES positions(id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id)
            REFERENCES departments(id)
            ON DELETE RESTRICT
);

CREATE INDEX idx_employees_position_id
    ON employees(position_id);

CREATE INDEX idx_employees_department_id
    ON employees(department_id);