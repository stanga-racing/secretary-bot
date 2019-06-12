INSERT INTO
  races (race_date,
         race_enrollment_deadline,
         race_name)
  VALUES
  (:race_date,
   :race_enrollment_deadline,
   :race_name)
ON CONFLICT (race_date, race_name)
  DO UPDATE SET
              race_date = excluded.race_date,
              race_name = excluded.race_name;
