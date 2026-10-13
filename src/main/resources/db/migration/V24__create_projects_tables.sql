-- =========================================================
-- PROJECTS
-- =========================================================

CREATE TABLE projects (
    id UUID PRIMARY KEY,
    agreement_number VARCHAR(50) NOT NULL UNIQUE,
    title TEXT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    period NUMERIC(19, 2),
    own_funds NUMERIC(19, 2),
    organization_contribution NUMERIC(19, 2),
    co_funders_contribution NUMERIC(19, 2),
    description TEXT,
    status VARCHAR(50) NOT NULL,

    region_id UUID NOT NULL,
    district_id UUID NOT NULL,
    municipality_id UUID NOT NULL,
    landscape_id UUID NOT NULL,
    sub_landscape_id UUID NOT NULL,
    support_partner_id UUID NOT NULL,
    local_organization_id UUID NOT NULL,
    strategic_plan_id UUID NOT NULL,

    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP
);


-- =========================================================
-- FOREIGN KEYS - PROJECTS
-- =========================================================

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_region
        FOREIGN KEY (region_id)
            REFERENCES regions(id);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_district
        FOREIGN KEY (district_id)
            REFERENCES districts(id);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_municipality
        FOREIGN KEY (municipality_id)
            REFERENCES municipalities(id);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_landscape
        FOREIGN KEY (landscape_id)
            REFERENCES landscapes(id);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_sub_landscape
        FOREIGN KEY (sub_landscape_id)
            REFERENCES sub_landscapes(id);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_support_partner
        FOREIGN KEY (support_partner_id)
            REFERENCES support_partners(id);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_local_organization
        FOREIGN KEY (local_organization_id)
            REFERENCES local_organizations(id);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_strategic_plan
        FOREIGN KEY (strategic_plan_id)
            REFERENCES strategic_plans(id);


-- =========================================================
-- PROJECT TOPICS
-- =========================================================

CREATE TABLE project_topics (
    project_id UUID NOT NULL,
    topic_id UUID NOT NULL,

    CONSTRAINT pk_project_topics
    PRIMARY KEY (project_id, topic_id)
);

ALTER TABLE project_topics
    ADD CONSTRAINT fk_project_topics_project
        FOREIGN KEY (project_id)
            REFERENCES projects(id)
            ON DELETE CASCADE;

ALTER TABLE project_topics
    ADD CONSTRAINT fk_project_topics_topic
        FOREIGN KEY (topic_id)
            REFERENCES topics(id)
            ON DELETE CASCADE;


-- =========================================================
-- PROJECT CO-FUNDERS
-- =========================================================

CREATE TABLE project_co_funders (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    co_funder_id UUID NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,

    CONSTRAINT uq_project_co_funder
        UNIQUE (project_id, co_funder_id)
);

ALTER TABLE project_co_funders
    ADD CONSTRAINT fk_project_co_funders_project
        FOREIGN KEY (project_id)
            REFERENCES projects(id)
            ON DELETE CASCADE;

ALTER TABLE project_co_funders
    ADD CONSTRAINT fk_project_co_funders_co_funder
        FOREIGN KEY (co_funder_id)
            REFERENCES co_funders(id);


-- =========================================================
-- GRANTED PROJECTS
-- =========================================================

CREATE TABLE granted_projects (
    id UUID PRIMARY KEY,
    grant_date DATE,
    grant_amount NUMERIC(19, 2),
    project_id UUID UNIQUE,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP
);

ALTER TABLE granted_projects
    ADD CONSTRAINT fk_granted_projects_project
        FOREIGN KEY (project_id)
            REFERENCES projects(id);

CREATE INDEX idx_projects_region_id
    ON projects(region_id);

CREATE INDEX idx_projects_district_id
    ON projects(district_id);

CREATE INDEX idx_projects_municipality_id
    ON projects(municipality_id);

CREATE INDEX idx_projects_landscape_id
    ON projects(landscape_id);

CREATE INDEX idx_projects_sub_landscape_id
    ON projects(sub_landscape_id);

CREATE INDEX idx_projects_support_partner_id
    ON projects(support_partner_id);

CREATE INDEX idx_projects_local_organization_id
    ON projects(local_organization_id);

CREATE INDEX idx_projects_strategic_plan_id
    ON projects(strategic_plan_id);

CREATE INDEX idx_project_co_funders_project_id
    ON project_co_funders(project_id);

CREATE INDEX idx_project_co_funders_co_funder_id
    ON project_co_funders(co_funder_id);

CREATE INDEX idx_granted_projects_project_id
    ON granted_projects(project_id);