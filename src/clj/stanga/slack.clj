(ns stanga.slack
  (:require [cheshire.core :as json]
            [clj-http.client :as http]))

(defn send-message [config msg]
  (let [webhook-url (:general-webhook-url config)
        body        (json/generate-string {:text msg})]
    (http/post webhook-url
               {:content-type :json
                :body         body})))
