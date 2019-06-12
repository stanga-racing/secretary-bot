(ns stanga.app
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.tools.logging :as log]
            [stanga.sheets-client :as sheets]
            [stanga.slack :as slack]))

(defprotocol App
  (run [this]))

(def date-formatter (f/formatter "dd.MM.yyyy"))

(def notification-treshold-days 3)

(defn- should-remind? [race]
  (when-let [deadline (:deadline race)]
    (t/within? (t/interval (t/today-at-midnight)
                           (t/from-now (t/days notification-treshold-days)))
               deadline)))

(defn- format-date [date]
  (f/unparse date-formatter date))

(defn- ->slack-message [race]
  (str (:name race)
       " ("
       (-> race :date format-date)
       "): ilmoittautumisten deadline: "
       (-> race :deadline format-date)))

(def xform-reminders (comp (filter should-remind?)
                           (map ->slack-message)))

(defrecord ReminderApp [config]
  App

  (run [this]
    (let [races (->> (sheets/get-races config)
                     (transduce xform-reminders conj))]
      (if (< 0 (count races))
        (let [msg (str "<!channel> Stangan automatisoitu sihteeri tässä hei! Seuraavien kilpailuiden ilmoittautumisten deadline lähestyy:"
                       "\n\n"
                       (clojure.string/join "\n" races))])
        ;(slack/send-message config msg))
        (log/info (str "No enrollment deadlines within next " notification-treshold-days " days"))))))
