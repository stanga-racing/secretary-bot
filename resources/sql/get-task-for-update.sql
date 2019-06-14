SELECT *
  FROM
    tasks
  WHERE
      task_execution_status = 'unhandled'
  AND task_execution_time <= NOW()
  AND task_execution_type :: VARCHAR = :task_execution_type
  LIMIT 1
    FOR UPDATE SKIP LOCKED;
