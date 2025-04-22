(defproject uap "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                  [org.clojure/core.async "1.6.673"];[org.clojure/core.async "1.0.567"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.xerial/sqlite-jdbc "3.30.1"]
                 [org.clojure/java.data "0.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [nrepl "1.0.0"]
                 ;[proto-repl "0.3.1"]
                 [seesaw "1.4.5"]
                 [org.jsoup/jsoup "1.11.2"]
                 [clj-soup/clojure-soup "0.1.3"]
                 [reply "0.3.7"]
                 [org.clojure/tools.namespace "1.4.4"]
                 [org.apache.commons/commons-lang3 "3.9"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.tcrawley/dynapath "1.1.0"]
                 [org.clojure/core.rrb-vector "0.1.2"]
                 [compliment "0.3.10"]
                 [mvxcvi/puget "1.2.0"]
                 [fipp "0.6.22"] [mvxcvi/arrangement "1.2.0"]
                 [criterium "0.4.5"]
                 [java-jdbc/dsl "0.1.3"]
                 [org.clojure/data.csv "1.0.0"]
                 [clj-commons/pomegranate "1.2.1"]
                 [com.github.seancorfield/honeysql "2.4.962"]
                 [clojure2d "1.4.4"]               
                 [org.seleniumhq.selenium/selenium-java "4.9.1"]
                 [org.seleniumhq.selenium/selenium-edge-driver "4.9.1"]
                 [org.seleniumhq.selenium/htmlunit-driver "4.9.0"]
                 [org.seleniumhq.selenium/selenium-support "4.9.1"]
                 [clojure.java-time "1.4.3"]
                 ;[org.seleniumhq.selenium/selenium-leg-rc "4.5.0"]
                 ;[org.webbitserver/webbit "0.4.15"]
                 ;[com.microsoft.sqlserver/mssql-jdbc "12.2.0.jre11"]
                 [com.microsoft.sqlserver/mssql-jdbc "11.2.3.jre17"]
                 [seancorfield/next.jdbc "1.0.0"]
                 [org.slf4j/jcl-over-slf4j "1.7.36"]
                 [saberstack/loop "0.2.3"]
                 [djblue/portal "0.58.5"]]

  :source-paths ["src" "dev"]
  :java-source-paths ["java_src"]
  :repl-options {:init-ns uap.core
;                 :nrepl-middleware [portal.nrepl/wrap-repl]
                 }
  :repl {:plugins [[cider/cider-nrepl "0.31.0"]
                   [refactor-nrepl "3.6.0"]
                   [lein-try "0.4.3"]]
         :dependencies [[nrepl "1.0.0"]
                       [alembic "0.3.2"]]}

;  :plugins [[lein-tools-deps "0.4.1"]]
;  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
;  :lein-tools-deps/config {:config-files [:install :user :project]}

  )
