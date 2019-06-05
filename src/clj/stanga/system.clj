(ns stanga.system
  (:require [com.stuartsierra.component :as component]
            [stanga.app :as app]
            [stanga.config :as config]
            [stanga.server :as server]))

(defn new-system []
  (component/system-map
    :app (component/using
           (app/map->ReminderApp {})
           [:config])

    :config (config/config)

    :server (component/using
              (server/map->Server {})
              [:app])))
