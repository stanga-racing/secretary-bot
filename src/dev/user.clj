(ns user
  (:require [reloaded.repl :refer [system init start stop go reset reset-all]]
            [stanga.system :as system]))

(reloaded.repl/set-init! #(system/new-system))
