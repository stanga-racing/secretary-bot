(ns stanga.races
  (:require [stanga.sheets-client :as sheets]
            [stanga.store.races-store :as races-store]))

(defn refresh-races-cache [config connection]
  (let [races (sheets/get-races config)]
    (doseq [race races]
      (races-store/upsert-race connection race))))

(defn get-races [connection]
  (races-store/get-races connection))
