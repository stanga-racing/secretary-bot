(ns stanga.races
  (:require [stanga.sheets-client :as sheets]
            [stanga.store.races-store :as races-store]))

(defn refresh-races-cache [config db]
  (let [races (sheets/get-races config)]
    (doseq [race races]
      (races-store/upsert-race db race))))

(defn get-races [db]
  (races-store/get-races db))
