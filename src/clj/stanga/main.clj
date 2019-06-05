(ns stanga.main
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [stanga.system :as system])
  (:gen-class))

(defn- add-shutdown-hook [system]
  (.addShutdownHook
    (Runtime/getRuntime)
    (Thread. (fn shutdown-system []
               (component/stop-system system)
               (log/info "System stopped")))))

(defn- wait-forever []
  @(promise))

(defn -main [& _]
  (let [system (component/start-system
                 (system/new-system))]
    (add-shutdown-hook system)
    (log/info "System started")
    (wait-forever)))
