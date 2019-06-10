(defproject secretary-bot "0.1.0"
  :description "Stanga Secretary Bot"
  :url "https://github.com/stanga-racing/secretary-bot"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/core.async "0.4.490"]
                 [org.clojure/tools.logging "0.4.1"]
                 [cheshire "5.8.1"]
                 [clj-http "3.10.0"]
                 [clj-time "0.15.0"]
                 [com.stuartsierra/component "0.4.0"]
                 [environ "1.1.0"]
                 [hikari-cp "2.7.1"]
                 [jarohen/chime "0.2.2"]
                 [net.cgrand/xforms "0.19.0"]
                 [org.postgresql/postgresql "42.2.5"]
                 [org.slf4j/slf4j-api "1.7.26"]
                 [org.slf4j/slf4j-log4j12 "1.7.26"]
                 [ragtime "0.8.0"]]
  :source-paths ["src/clj"]
  :main ^:skip-aot stanga.main
  :aot [stanga.main]
  :profiles {:dev {:dependencies [[reloaded.repl "0.2.4"]]
                   :repl-options {:init-ns user}
                   :source-paths ["src/dev"]}}
  :aliases {"undo-migration" ["trampoline" "run" "-m" "undo-migration"]})
