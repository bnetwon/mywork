(defproject myscript "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :main swing-clip.clipbord
  :dependencies [[org.clojure/clojure "1.8.0"]
                   [dk.ative/docjure "1.12.0"]
                   [org.clojure/core.async "0.3.443"]
                   [org.clojure/clojure-contrib "1.2.0"]
                   [proto-repl "0.3.1"]
                   [seesaw "1.4.5"]
                   [org.jsoup/jsoup "1.11.2"]
                   [clj-soup/clojure-soup "0.1.3"]
                   [org.clojure/core.async "0.4.490"]
                   [reply "0.3.7"]
                   [org.clojure/tools.namespace "0.3.1"]
                   [org.apache.commons/commons-lang3 "3.9"]
                   [org.tcrawley/dynapath "1.0.0"]
]
    ;:resource ["lib/*.jar" "lib/*.class"]
    :resource-paths ["lib/jacob.jar"
                     "lib/byte-buddy-1.8.15.jar"
                     "lib/client-combined-3.141.59.jar"
                     "lib/commons-beanutils-1.9.3.jar"
                     "lib/commons-exec-1.3.jar"
                     "lib/commons-net-3.6.jar"
                     "lib/guava-25.0-jre.jar"
                     "lib/okhttp-3.11.0.jar"
                     "lib/okio-1.14.0.jar"
                     "lib/Dev21refreshTest.class"]
    ;:resource-paths ["lib/ij.jar"]
    :namespace ["swing-clip.clipbord"]
)
