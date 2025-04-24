(ns swing-clip.sql-db 
 (:require [clojure.tools.namespace.repl :as tnr]
            [java-jdbc.sql]
            [java-time]
            [honey.sql.helpers :as h]
            [clojure.java.jdbc :as jdbc])
  )
;;(refer-clojure :exclude '[filter for group-by into partition-by set update])
(require '[honey.sql :as sql]
         ;; CAUTION: this overwrites several clojure.core fns:
         ;;
         ;; filter, for, group-by, into, partition-by, set, and update
         ;;
         ;; you should generally only refer in the specific
         ;; helpers that you want to use!
         ;;'[honey.sql.helpers :refer :all :as h]
         '[honey.sql.helpers :as h]
         ;; so we can still get at clojure.core functions:
         '[clojure.core :as c])


;; (def libdb
;;   {:classname   "org.sqlite.JDBC"
;;    :subprotocol "sqlite"
;;    :subname     "db/lib_db.db"})

(def cdb
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/sql-db.db"} )

(defn getdbtablename [^String pstr  ]
(let 
    [tstr  (-> (clojure.string/trim pstr)
               (clojure.string/upper-case ))]
  (clojure.java.jdbc/query cdb 
    (-> (h/select :tablenames.*)
        (h/from :tablenames)
        (h/where [:=  :tablenames.name_lo tstr ])
        (sql/format)))))


(defn static? [field]
  (java.lang.reflect.Modifier/isStatic
   (.getModifiers field)))

(defn get-record-field-names [record]
  (->> record
       .getDeclaredFields
       (remove static?)
       (map #(.getName %))
       (remove #{"__meta" "__extmap"})))

(defmacro empty-record [record]
  (let [klass (Class/forName (name record))
        field-count (count (get-record-field-names klass))]
    `(new ~klass ~@(repeat field-count nil))))

(defn getclassname [f] (last (clojure.string/split (str (class f)) #"\.")))
(defn getselectall[f] (let [ft (if (seq? f) (first f) f)
                               fv (if (seq? f) f (vector f)) ]
(
 -> (h/select :*)
   (h/from (keyword (getclassname ft)))
   ;(columns  (map keyword (get-record-field-names f)))
;   (h/values fv)
)))

(defn getinsertstruct[f] (let [ft (if (seq? f) (first f) f)
                               fv (if (seq? f) f (vector f)) ]
(
-> (h/insert-into (keyword (getclassname ft)))
   ;(columns  (map keyword (get-record-field-names f)))
   (h/values fv)
)))

(defn adddatetime [record-data] (let [currtime (java-time/local-date-time)] (assoc record-data :create-datetime currtime :update-datetime currtime)))
(defn gettablekeyword [sqlst] (let [tkeyword (if (contains? sqlst :insert-into) :insert-into :from )  ] tkeyword))
(defn execute 
([sqlst] (execute cdb sqlst))
([tmpdb sqlst]  (jdbc/execute! tmpdb  (sql/format sqlst))))
(defn query 
  ([sqlst] (query cdb sqlst))
  ([tmpdb sqlst]  (jdbc/query tmpdb  (sql/format sqlst))))


;; ;; get old 3 data
;; (def query-select (-> (helpers/select :id)
;;                       (helpers/from :Foo)
;;                       (helpers/order-by [:createDate :asc])
;;                       (helpers/limit 3)))

;; (def ids-to-delete (jdbc/query db-spec (sql/format query-select)))

;; ;; delete data
;; (def query-delete (-> (helpers/delete-from :Foo)
;;                       (helpers/where [:in :id (map :id ids-to-delete)])))


; (jdbc/execute! cdb  (sql/format (sdb/getinsertstruct f) ))
; (jdbc/query sdb/cdb (sql/format (sdb/getselectall f)))

;;(def sqlmap {:select [:a :b :c]
;;             :from   [:foo]
;;             :where  [:= :foo.a "baz"]})
;;(sql/format sqlmap)
;;(sql/format sqlmap {:inline true})
;;(sql/format sqlmap {:numbered true})
;;=> ["SELECT a, b, c FROM foo WHERE foo.a = ?" "baz"]
;;;; sqlmap as symbols instead of keywords:
;;(-> '{select (a, b, c) from (foo) where (= foo.a "baz")}
;;    (sql/format))
;;=> ["SELECT a, b, c FROM foo WHERE foo.a = ?" "baz"]
;;(jdbc/execute! conn (sql/format sqlmap))
;;(def q-sqlmap {:select [:foo/a :foo/b :foo/c]
;;               :from   [:foo]
;;               :where  [:= :foo/a "baz"]})
;;(sql/format q-sqlmap)
;;(-> (select :a :b :c)
;;    (from :foo)
;;    (where [:= :foo.a "baz"]))
;;=> {:select [:a :b :c] :from [:foo] :where [:= :foo.a "baz"]}


;; (defn select-cdb
;;   ([selsym]
;;     ; ^clojure.lang.Keyword sym]

;;      (java-jdbc.sql/select selsym)))
;;   ([selsym
;;     ^clojure.lang.Keyword sym
;;     whereopt]
;;    (clojure.java.jdbc/query cdb
;;      (java-jdbc.sql/select selsym sym (java-jdbc.sql/where whereopt))))) 
