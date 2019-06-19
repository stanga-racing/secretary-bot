(ns stanga.app
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.tools.logging :as log]
            [stanga.db :as d]
            [stanga.races :as races]
            [stanga.slack :as slack]
            [stanga.store.tasks-store :as tasks-store]
            [stanga.store.race-notifications-store :as race-notifications-store]
            [stanga.store.races-store :as races-store]
            [stanga.time :as time]))

(defprotocol App
  (run [this]))

(def date-formatter (f/formatter "dd.MM.yyyy"))

(def notification-treshold-days 2)

(defn- should-remind? [race]
  (when-let [deadline (:race-enrollment-deadline race)]
    (t/within? (t/interval (t/today-at-midnight)
                           (t/from-now (t/days notification-treshold-days)))
               deadline)))

(defn- format-date [date]
  (when date
    (f/unparse date-formatter date)))

(defn- race->slack-message [race]
  (str (:race-name race)
       " ("
       (-> race :race-date format-date)
       "): ilmoittautumisten deadline: "
       (-> race :race-enrollment-deadline format-date)))

(defn- format-slack-message [messages]
  (str "<!maantie_kisaajat> Stangan automatisoitu sihteeri tässä hei! Seuraavien kilpailuiden ilmoittautumisten deadline lähestyy:"
       "\n\n"
       (clojure.string/join "\n" messages)))

(defrecord ReminderApp [config db-pool]
  App

  (run [this]
    (d/with-transaction [db db-pool]
      (races/refresh-races-cache config db))
    (d/with-transaction [db db-pool]
      (when-let [task (tasks-store/get-task-for-update db :race-notification)]
        (try
          (let [races (->> (races-store/get-races db)
                           (filter should-remind?))]
            (if (< 0 (count races))
              (do
                (let [msg (->> races
                               (map race->slack-message)
                               (format-slack-message))]
                  (slack/send-message config msg))
                (doseq [race races]
                  (race-notifications-store/insert-race-notification db
                                                                     {:race-id (:race-id race)
                                                                      :task-id (:task-id task)})))
              (log/info (str "No enrollment deadlines within next " notification-treshold-days " days"))))
          (tasks-store/update-task db
                                   (assoc task :task-execution-status :processed))
          (catch Error e
            (log/error "Failed to process task " (:task-id task) ": " e)
            (tasks-store/update-task db
                                     (assoc task :task-execution-status :error))))
        (tasks-store/insert-task db
                                 {:task-execution-time   (-> (t/days 1)
                                                             (t/from-now)
                                                             (time/at-midday))
                                  :task-execution-type   :race-notification
                                  :task-execution-status :unhandled})))))

