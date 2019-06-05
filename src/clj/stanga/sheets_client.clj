(ns stanga.sheets-client
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [net.cgrand.xforms :as xf]))

(def base-url "https://sheets.googleapis.com/v4")

(def date-format (f/formatter "dd.MM.yyyy"))

(defn- parse-date [date-str]
  (try
    (f/parse date-format (str date-str "2019"))
    (catch Exception _)))

(defn- normalize-date [date-str]
  (let [[full-str _ normalized-date month] (re-matches #"^((\d{1,2}\.)-\d{1,2}\.)?(.*)$" date-str)]
    (cond (not (clojure.string/blank? normalized-date))
          (str normalized-date month)

          (not (clojure.string/blank? full-str))
          full-str

          :else
          date-str)))

(defn- future-date? [{date :date}]
  (t/before? (t/now) date))

(defn- get-url [path]
  (str base-url path))

(defn- get* [config path query-params]
  (let [api-key (:master-excel-api-key config)
        url     (get-url path)
        resp    (http/get url {:query-params (assoc query-params "key" api-key)})]
    (if (= (:status resp) 200)
      (-> (:body resp)
          (json/parse-string true))
      (throw (Exception. (str "HTTP GET " path " failed: " resp))))))

(def xform-races (comp (map :values)
                       (map first)
                       (map (partial zipmap [:date :deadline :name]))
                       (map #(update % :date (comp parse-date normalize-date)))
                       (map #(update % :deadline (comp parse-date)))
                       (filter future-date?)
                       (xf/sort-by :date)))

(defn get-races [config]
  (let [spreadsheet-id (:master-excel-spreadsheet-id config)
        path           (str "/spreadsheets/" spreadsheet-id "/values:batchGet")
        query-params   {:ranges (->> (range 4 58)
                                     (map (fn ->range-param [idx]
                                            (str "Kisat 2019!A" idx ":C" idx))))}]
    (->> (get* config path query-params)
         :valueRanges
         (transduce xform-races conj))))
