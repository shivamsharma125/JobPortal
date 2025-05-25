CREATE TABLE jobs
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime              NULL,
    last_modified_at datetime              NULL,
    state            SMALLINT              NULL,
    position         VARCHAR(255)          NULL,
    `description`    VARCHAR(2000)         NULL,
    min_experience   INT                   NOT NULL,
    max_experience   INT                   NOT NULL,
    min_salary       DOUBLE                NOT NULL,
    max_salary       DOUBLE                NOT NULL,
    notice_period    INT                   NOT NULL,
    posted_at        datetime              NULL,
    posted_by_id     BIGINT                NULL,
    CONSTRAINT pk_jobs PRIMARY KEY (id)
);

CREATE TABLE jobs_skills
(
    job_id   BIGINT NOT NULL,
    skill_id BIGINT NOT NULL
);

CREATE TABLE skills
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime              NULL,
    last_modified_at datetime              NULL,
    state            SMALLINT              NULL,
    name             VARCHAR(255)          NULL,
    CONSTRAINT pk_skills PRIMARY KEY (id)
);

ALTER TABLE jobs
    ADD CONSTRAINT FK_JOBS_ON_POSTED_BY FOREIGN KEY (posted_by_id) REFERENCES users (id);

ALTER TABLE jobs_skills
    ADD CONSTRAINT fk_jobskil_on_job FOREIGN KEY (job_id) REFERENCES jobs (id);

ALTER TABLE jobs_skills
    ADD CONSTRAINT fk_jobskil_on_skill FOREIGN KEY (skill_id) REFERENCES skills (id);