(ns stanga.time
  (:require [clj-time.core :as t]))

(defn at-midday [date]
  (let [year  (t/year date)
        month (t/month date)
        day   (t/day date)]
    (t/date-time year
                 month
                 day
                 12
                 0
                 0)))