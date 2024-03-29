CREATE TABLE races
(
  race_id                  BIGSERIAL NOT NULL,
  race_date                TIMESTAMP NOT NULL,
  race_enrollment_deadline TIMESTAMP,
  race_name                VARCHAR   NOT NULL,
  PRIMARY KEY (race_id),
  UNIQUE (race_date, race_name)
);