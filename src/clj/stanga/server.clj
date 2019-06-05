(ns stanga.server
  (:require [chime :as c]
            [clj-time.core :as t]
            [clj-time.periodic :as p]
            [clojure.core.async :as async]
            [com.stuartsierra.component :as component]))

(defn- run-time [date]
  (let [year  (t/year date)
        month (t/month date)
        day   (t/day date)]
    (t/date-time year
                 month
                 day
                 17
                 0
                 0)))

(defrecord Server [app]
  component/Lifecycle

  (start [this]
    (let [scheduler (c/chime-ch
                      (->> (p/periodic-seq (t/from-now (t/days 1))
                                           (t/days 1))
                           (map run-time)
                           (cons (t/from-now (t/seconds 1))))
                      {:ch (async/chan
                             (async/sliding-buffer 1))})]
      (async/go-loop []
        (when-let [_ (async/<! scheduler)]
          (try
            (.run app)
            (catch Exception e
              (println (str "Error: " e))))
          (recur)))
      (assoc this :scheduler scheduler)))

  (stop [this]
    (when-let [scheduler (:scheduler this)]
      (async/close! scheduler))
    (assoc this :scheduler nil)))
