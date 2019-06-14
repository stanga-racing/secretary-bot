(ns stanga.server
  (:require [chime :as c]
            [clj-time.core :as t]
            [clj-time.periodic :as p]
            [clojure.core.async :as async]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [stanga.time :as time]))

(defrecord Server [app]
  component/Lifecycle

  (start [this]
    (let [scheduler (c/chime-ch
                      (->> (p/periodic-seq (t/from-now (t/days 1))
                                           (t/days 1))
                           (map time/at-midday)
                           (cons (t/from-now (t/seconds 1))))
                      {:ch (async/chan
                             (async/sliding-buffer 1))})]
      (async/go-loop []
        (when-let [_ (async/<! scheduler)]
          (try
            (.run app)
            (catch Exception e
              (log/error (str "Error: " e))))
          (recur)))
      (assoc this :scheduler scheduler)))

  (stop [this]
    (when-let [scheduler (:scheduler this)]
      (async/close! scheduler))
    (assoc this :scheduler nil)))
