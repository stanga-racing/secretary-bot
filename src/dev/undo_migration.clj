(ns undo-migration
  (:require [ragtime.repl :as migrations]
            [stanga.config :as config]
            [stanga.migrations :as migration-config]))

(defn -main [& _]
  (let [migration-config (migration-config/migration-config
                           (config/config))]
    (migrations/rollback migration-config)))
