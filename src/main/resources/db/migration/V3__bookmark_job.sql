CREATE TABLE jobs_bookmarks
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime              NULL,
    last_modified_at datetime              NULL,
    state            SMALLINT              NULL,
    user_id          BIGINT                NOT NULL,
    job_id           BIGINT                NOT NULL,
    bookmarked_at    datetime              NOT NULL,
    CONSTRAINT pk_jobs_bookmarks PRIMARY KEY (id)
);

ALTER TABLE jobs_bookmarks
    ADD CONSTRAINT uc_7c87fe6160244ee725ee054cb UNIQUE (user_id, job_id);

ALTER TABLE jobs_bookmarks
    ADD CONSTRAINT FK_JOBS_BOOKMARKS_ON_JOB FOREIGN KEY (job_id) REFERENCES jobs (id);

ALTER TABLE jobs_bookmarks
    ADD CONSTRAINT FK_JOBS_BOOKMARKS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);