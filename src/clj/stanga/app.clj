(ns stanga.app
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [stanga.sheets-client :as sheets]
            [stanga.slack :as slack]))

(defprotocol App
  (run [this]))

(def date-formatter (f/formatter "dd.MM.yyyy"))

(defn- should-remind? [race]
  (when-let [deadline (:deadline race)]
    (t/within? (t/interval (t/now)
                           (t/from-now (t/days 4)))
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
      (when (< 0 (count races))
        (let [msg (str "<!channel> Stangan automatisoitu sihteeri tässä hei! Seuraavien kilpailuiden ilmoittautumisten deadline lähestyy:"
                       "\n\n"
                       (clojure.string/join "\n" races))]
          (slack/send-message config msg))))))
