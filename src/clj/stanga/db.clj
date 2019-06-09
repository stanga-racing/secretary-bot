(ns stanga.db
  (:require [com.stuartsierra.component :as component]
            [hikari-cp.core :as pool]))

(defrecord DbPool [config]
  component/Lifecycle

  (start [this]
    (let [db-config (:database config)
          db-spec   {:adapter           (:adapter db-config)
                     :auto-commit       false
                     :database-name     (:database db-config)
                     :maximum-pool-size 10
                     :minimum-idle      10
                     :password          (:password db-config)
                     :port-number       (:port db-config)
                     :server-name       (:hostname db-config)
                     :username          (:username db-config)}]
      (assoc this :datasource (pool/make-datasource db-spec))))

  (stop [this]
    (when-let [datasource (:datasource this)]
      (pool/close-datasource datasource))
    (assoc this :datasource nil)))
