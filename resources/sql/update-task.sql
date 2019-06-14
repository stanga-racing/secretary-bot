UPDATE tasks
SET
  task_execution_status = :task_execution_status :: TASK_EXECUTION_STATUS_T
  WHERE
    task_id = :task_id
