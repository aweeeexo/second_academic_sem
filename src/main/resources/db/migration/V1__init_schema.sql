CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       completed BOOLEAN NOT NULL DEFAULT FALSE,
                       priority VARCHAR(20),
                       due_date DATE,
                       tags TEXT,
                       created_date TIMESTAMP NOT NULL,
                       last_modified_date TIMESTAMP NOT NULL
);

CREATE TABLE task_attachments (
                                  id BIGSERIAL PRIMARY KEY,
                                  file_name VARCHAR(255) NOT NULL,
                                  file_path VARCHAR(512) NOT NULL,
                                  file_size BIGINT NOT NULL,
                                  task_id BIGINT NOT NULL,
                                  CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);