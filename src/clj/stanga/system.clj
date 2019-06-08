(ns stanga.system
  (:require [com.stuartsierra.component :as component]
            [stanga.app :as app]
            [stanga.config :as config]
            [stanga.db :as db]
            [stanga.migrations :as migrations]
            [stanga.server :as server]))

(defn new-system []
  (component/system-map
    :app (component/using
           (app/map->ReminderApp {})
           [:config :db])

    :config (config/config)

    :db (component/using
          (db/map->DbPool {})
          [:config :migrations])

    :migrations (component/using
                  (migrations/map->MigrationRunner {})
                  [:config])

    :server (component/using
              (server/map->Server {})
              [:app])))
