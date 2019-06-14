CREATE TYPE TASK_EXECUTION_STATUS_T AS ENUM (
  'unhandled',
  'processed',
  'error'
  );
CREATE TYPE TASK_EXECUTION_TYPE_T AS ENUM (
  'race_notification'
  );
CREATE TABLE tasks
(
  task_id               BIGSERIAL               NOT NULL,
  task_execution_time   TIMESTAMP               NOT NULL,
  task_execution_type   TASK_EXECUTION_TYPE_T   NOT NULL,
  task_execution_status TASK_EXECUTION_STATUS_T NOT NULL,
  PRIMARY KEY (task_id)
);
