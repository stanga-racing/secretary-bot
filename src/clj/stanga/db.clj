(ns stanga.db
  (:require [com.stuartsierra.component :as component]
            [hikari-cp.core :as pool]))

(defrecord DbPool [config]
  component/Lifecycle

  (start [this]
    (let [db-spec {:adapter           "postgresql"
                   :auto-commit       false
                   :jdbc-url          (-> config :database :url)
                   :maximum-pool-size 10
                   :minimum-idle      10
                   :username          (-> config :database :username)
                   :password          (-> config :database :password)}]
      (assoc this :datasource (pool/make-datasource db-spec))))

  (stop [this]
    (when-let [datasource (:datasource this)]
      (pool/close-datasource datasource))
    (assoc this :datasource nil)))
