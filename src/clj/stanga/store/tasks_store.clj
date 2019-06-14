(ns stanga.store.tasks-store
  (:require [stanga.db :as db]
            [yesql.core :as sql]))

(sql/defquery get-task-for-update* "sql/get-task-for-update.sql")
(sql/defquery update-task! "sql/update-task.sql")
(sql/defquery insert-task! "sql/insert-task.sql")

(defn get-task-for-update [db task-execution-type]
  (->> (db/exec db
                get-task-for-update*
                {:task-execution-type task-execution-type})
       (first)))

(defn update-task [db task]
  (db/exec db
           update-task!
           task))

(defn insert-task [db task]
  (db/exec db
           insert-task!
           task))
