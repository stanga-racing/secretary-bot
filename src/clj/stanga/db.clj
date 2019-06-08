(ns stanga.db
  (:require [com.stuartsierra.component :as component]
            [hikari-cp.core :as pool]))

(defrecord DbPool [config]
  component/Lifecycle

  (start [this]
    (let [db-config (:database config)
          db-spec   (merge {:auto-commit       false
                            :maximum-pool-size 10
                            :minimum-idle      10}
                           (select-keys db-config [:adapter
                                                   :jdbc-url
                                                   :username
                                                   :password]))]
      (assoc this :datasource (pool/make-datasource db-spec))))

  (stop [this]
    (when-let [datasource (:datasource this)]
      (pool/close-datasource datasource))
    (assoc this :datasource nil)))
