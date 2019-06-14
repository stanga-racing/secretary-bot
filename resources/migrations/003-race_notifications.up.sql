CREATE TABLE race_notifications
(
  race_notification_id BIGSERIAL NOT NULL,
  race_id              BIGINT    NOT NULL REFERENCES races (race_id),
  task_id              BIGINT    NOT NULL REFERENCES tasks (task_id),
  PRIMARY KEY (race_notification_id),
  UNIQUE (race_id, task_id)
);
