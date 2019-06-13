(ns stanga.store.races-store
  (:require [clj-time.coerce :as t]
            [clojure.tools.logging :as log]
            [stanga.db :as db]
            [yesql.core :as sql]))

(sql/defquery upsert-race<! "sql/upsert-race.sql")
(sql/defquery get-races* "sql/get-races.sql")

(defn upsert-race [db race]
  (let [result (db/exec db
                        upsert-race<!
                        (update race :race-date t/to-sql-date))]
    (log/info (str "UPSERT race: " result))))

(defn get-races [db]
  (db/exec db get-races* {}))

