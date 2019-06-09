(ns stanga.migrations
  (:require [com.stuartsierra.component :as component]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as migrations]))

(defn migration-config [config]
  (let [db-config (:database config)
        db-spec   {:dbtype   (-> db-config :adapter)
                   :dbname   (-> db-config :database)
                   :host     (-> db-config :hostname)
                   :user     (-> db-config :username)
                   :password (-> db-config :password)
                   :port     (-> db-config :port)}]
    {:datastore  (jdbc/sql-database db-spec)
     :migrations (jdbc/load-resources "migrations")}))


(defrecord MigrationRunner [config]
  component/Lifecycle

  (start [this]
    (let [config (migration-config config)]
      (migrations/migrate config))
    this)

  (stop [this]
    this))
