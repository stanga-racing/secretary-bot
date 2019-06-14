INSERT INTO
  tasks (task_execution_time,
         task_execution_type,
         task_execution_status)
  VALUES
  (:task_execution_time,
   :task_execution_type :: TASK_EXECUTION_TYPE_T,
   :task_execution_status :: TASK_EXECUTION_STATUS_T)
