(ns stanga.config
  (:require [environ.core :as e]))

(defn- make-database-config []
  (let [[_
         _
         _
         username
         password
         hostname
         port
         database] (clojure.string/split
                     (e/env :database-url)
                     #"[:\/@]")]
    {:adapter  "postgresql"
     :database database
     :hostname hostname
     :username username
     :password password
     :port     port}))

(defn config []
  {:database                    (make-database-config)
   :general-webhook-url         (e/env :stanga-general-webhook-url)
   :master-excel-api-key        (e/env :stanga-master-excel-api-key)
   :master-excel-spreadsheet-id (e/env :stanga-master-excel-spreadsheet-id)})
