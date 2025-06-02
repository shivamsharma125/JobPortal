CREATE TABLE job_applications
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime              NULL,
    last_modified_at datetime              NULL,
    state            SMALLINT              NULL,
    job_id           BIGINT                NOT NULL,
    applicant_id     BIGINT                NOT NULL,
    resume_url       VARCHAR(255)          NOT NULL,
    status           SMALLINT              NOT NULL,
    applied_at       datetime              NOT NULL,
    CONSTRAINT pk_job_applications PRIMARY KEY (id)
);

ALTER TABLE job_applications
    ADD CONSTRAINT uc_def0be5735a66ca3625916563 UNIQUE (job_id, applicant_id);

ALTER TABLE job_applications
    ADD CONSTRAINT FK_JOB_APPLICATIONS_ON_APPLICANT FOREIGN KEY (applicant_id) REFERENCES users (id);

ALTER TABLE job_applications
    ADD CONSTRAINT FK_JOB_APPLICATIONS_ON_JOB FOREIGN KEY (job_id) REFERENCES jobs (id);