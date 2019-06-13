(ns stanga.db
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cse]
            [clj-time.coerce :as c]
            [clojure.java.jdbc :as jdbc]
            [com.stuartsierra.component :as component]
            [hikari-cp.core :as pool]))

(defn- ->sql-time [x]
  (cond-> x
    (= (class x) org.joda.time.DateTime)
    (c/to-sql-time)))

(defn ->joda-time [x]
  (cond-> x
    (= (class x) java.sql.Timestamp)
    (c/to-date-time)))

(defn- coerce->db [params]
  (->> params
       (clojure.walk/prewalk ->sql-time)
       (cse/transform-keys csk/->snake_case)))

(defn- coerce->clj [result]
  (->> result
       (clojure.walk/prewalk ->joda-time)
       (cse/transform-keys csk/->kebab-case)))

(defn exec [connection query params]
  (let [coerced-params (coerce->db params)
        result         (query coerced-params connection)]
    (coerce->clj result)))

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
