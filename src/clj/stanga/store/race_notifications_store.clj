(ns stanga.store.race-notifications-store
  (:require [stanga.db :as db]
            [yesql.core :as sql]))

(sql/defquery insert-race-notification! "sql/insert-race-notification.sql")

(defn insert-race-notification [db race-notification]
  (db/exec db
           insert-race-notification!
           race-notification))
