(ns stanga.config
  (:require [clojure.spec.alpha :as spec]
            [environ.core :as e]))

(spec/check-asserts true)

(spec/def ::master-excel-api-key string?)
(spec/def ::master-excel-spreadsheet-id string?)
(spec/def ::general-webhook-url string?)

(spec/def ::config (spec/keys :req-un [::master-excel-api-key
                                       ::master-excel-spreadsheet-id
                                       ::general-webhook-url]))

(defn config []
  {:post [(spec/valid? ::config %)]}
  {:general-webhook-url         (e/env :stanga-general-webhook-url)
   :master-excel-api-key        (e/env :stanga-master-excel-api-key)
   :master-excel-spreadsheet-id (e/env :stanga-master-excel-spreadsheet-id)})
