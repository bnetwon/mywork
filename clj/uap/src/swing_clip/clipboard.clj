
(ns swing-clip.clipboard
  (:require [clojure.core.async] )
  (:import [java.awt Toolkit]
           [java.awt.datatransfer Clipboard DataFlavor StringSelection])
  (:use [clojure.tools.namespace.repl :only [refresh]] ))
(defn flip [f]
  (fn [& xs]
    (apply f (reverse xs))))
(def split-flip 
  (flip clojure.string/split))

;(import [java.awt Toolkit] [java.awt.datatransfer Clipboard DataFlavor StringSelection]])
;(.replace (get-string) "\n" "" )
;(def atom-recur (atom true))
;(require 'inspector.core)
;(require '[inspector.core :refer [?]])
;(partition 2 (map #(Integer/parseInt %) (clojure.string/split nums #" ")))
(def env (atom { :clip-watch true , :clip-sleep 2000 }))
(def oldclipms (atom 0))
(def newclipms (atom 0))
(def currclip (atom ""))

(def ^Clipboard clip (.getSystemClipboard (Toolkit/getDefaultToolkit)))
(defn get-string []
  (when (.isDataFlavorAvailable clip DataFlavor/stringFlavor)
    (.getData clip DataFlavor/stringFlavor)))

(defn get-filelist []
  (when (.isDataFlavorAvailable clip DataFlavor/javaFileListFlavor)
    (.getData clip DataFlavor/javaFileListFlavor)))

(def a 0)
(defn cnttt[stra] (if (= (count stra) 0) (println "") (do (def a (+ a 1)) (println (str a)))))
(defn cntttllist[stra cntnum]( let [
                                    nelement (first stra)
                                    nelementcnt (count nelement)
                                    calcnt (if (= nelementcnt 0 ) cntnum (+ cntnum 1))]
                              (if (= nelement nil) nil
                               (cons (if (= nelementcnt 0 ) nil calcnt)
                                (cntttllist (rest stra) calcnt)))))

(defn addalstr[strarr linesep] (if (= (count strarr) 0) ""
                                (str (if (first strarr) (first strarr) "" ) linesep
                                 (addalstr (rest strarr) linesep))))
(defn getlist []
    (clojure.string/split (get-string) #"\n"))

(defn makesomelist []
  (do (def somelist
        (clojure.string/split (get-string) #"\n"))
    somelist))
;(def somelist (clojure.string/split (get-string) #"\n"))

(defn set-string [s]
  (let [ss (StringSelection. (print-str s))]
    (.setContents clip ss ss)))
(defn clip-change [tfn] (set-string (tfn (get-string))))
(defn change[ a b ](set-string (clojure.string/replace (get-string) a b)))
(defn changes[ a b ](set-string (clojure.string/replace (get-string) a b)))
(defn set-list[s] (set-string (clojure.string/join "\n" s)))
(defn ddd [strings] (partition 2 (map #(str %) (clojure.string/split strings #"(\t|\n)"))))
      
(defn stringtomap [strings] (into {} (map vec (partition 2 (map #(str %) (clojure.string/split strings #"(\t|\n)"))))))
            
(defn stringtomapbyreg [strings regx] (into {} (map vec (partition 2 (map #(str %) (clojure.string/split strings regx))))))

;(def mrr (into {} (map vec rr)))
;(def mrr (into {} (map vec (ddd (get-string)))))

;(defn loopcl[](do (set-string (.replace (get-string) "\n" "" )) (.sleep Thread 1000)(if atom-recur recur nil)  ) )
;(def resultlist (map #(let [n %]( if (> n -1) (maptlist n) "" )) (map #(.indexOf mapslist %) alllist  )))
; (set-string (clojure.string/join "\n"  resultlist))
(defn rrten[ r rs ] (if (re-find r rs) rs))
(defn listtostring[list](clojure.string/join "\n" list  ))
; (set-string (clojure.string/join "\n" (map  #(clojure.string/replace insertsql #"'105','6660'" (str "'105','"  %1 "'")) storelist)))
(use 'clojure.java.io)
; (defn make-regexp [ext]
;   (java.util.regex.Pattern/compile
;     (str ".*\\." ext "$")))
; (map #(.indexOf alllist %) mapslist )
(defn listfn[tmplist cntfilter & restfilter]
        (map #(doall (doseq [rfilter restfilter] ( rfilter %))( cntfilter %)))
  tmplist)
(def listcountmap ( let [cnt  (atom 0)
                         ccnt (fn [content](if (> (count content) 0) (swap! cnt inc) ""))
                         rcnt (fn [content](if false (swap! cnt (constantly 0))))]
                    {:cnt cnt :ccnt ccnt :rcnt rcnt}))
;(listfn makesomelist (listcountmap :cnt) (listcountmap :ccnt) (listcountmap :rcnt))
(defn cnt->map[] (set-string
                   (reduce #(str %1 %2 "\n") 
                     "" 
                     (listfn somelist (listcountmap :ccnt) (listcountmap :rcnt)))))


(import '(java.io File))
(def targetdir (File. "C:/Users/user1/Desktop/bmnhosei/mixup"))
(def destdir (File. "C:/Users/user1/Desktop/bmnhosei/dest"))
(def targetlist (.listFiles targetdir))

;(defn convert[targerfile] (with-open [fin (reader targerfile)]
;   (with-open [fout (writer  (File. destdir (.getName targerfile)) :append true :encoding "UTF-8" )]
;  (doseq [strr (line-seq fin)]
;    (let [strtext (rrten swing-clip.datastore/finestore strr ) ] (if strtext ( .write fout (str strtext "\r\n") )))
;    )
;  )))
;  (map convert targetlist)
  ; (with-open [fout (writer (File. destdir  "targerfile")  )]
  ;
  ;   (.write fout "ff" )
  ;   )
  ;   (defn write-csv-file
  ;     "Writes a csv file using a key and an s-o-s (sequence of sequences)"
  ;     [out-sos out-file]
  ;
  ;     (spit out-file "" :append false)
  ;     (with-open [out-data (io/writer out-file)]
  ;         (csv/write-csv out-data out-sos)))
; (defn list-files-by-ext [ext]
;   (filter #(re-seq (make-regexp ext) %)
;           (map #(.getName %) (.listFiles (File. ".")))))
; (with-open [fin (reader "test.txt")]
;   (doseq [str (line-seq fin)]
;     (println str))
;   )
; (with-open [fout (writer  "hello.txt" :append true :encoding "UTF-8" )]
;   (.write fout (str "hello" " world"))
;   )

  ; (seq (filter #(re-find (->> (str str-input)
  ;                                          (upper-case)
  ;                                          (re-pattern))
  ;                                     (upper-case (k %)))
  ;                      @data

; (println (rrten datastore/finestore "201706201626200000800110000001R32321945972017062000000100000023000025    +000010  0200009700000000000000005  ")

; (Proto-repl-charts.charts/line-chart
;  "Trigonometry"
;  {"sin" (map #(Math/sin %) (range 0.0 6.0 ;0.2))
;   "cos" (map #(Math/cos %) (range 0.0 6.0 0.2))})
;   (let [input-values (range 0.0 6.0 0.5)]
;   (proto-repl-charts.charts/line-chart
;    "Trigonometry"
;    {"sin" (map #(Math/sin %) input-values)
;     "cos" (map #(Math/cos %) input-values)}
;    {:labels input-values}))
;

(def regex-char-esc-smap
  (let [esc-chars "()*&^%$#!"]
    (zipmap esc-chars
            (map #(str "\\" %) esc-chars))))

(defn str-to-pattern
  [string]
  (->> string
       (replace regex-char-esc-smap)
       (reduce str)
       re-pattern))
;-table
(use '[clojure.string :only [split split-lines trim]])


(defn tokens
  [s]
;  (-> s trim (split #"\s+")))
  (-> s trim (split #"\s+")))

(defn pairs
  [coll1 coll2]
  (map vector coll1 coll2))


(defn parse-table
  [raw-table-data]
  (let [table-data (map tokens (split-lines raw-table-data))
        column-names (first table-data)
        row-names (map first (next table-data))
        contents (map next (next table-data))]
    (apply merge-with merge
      (for [[row-name row-contents] (pairs row-names contents)
            [column-name element] (pairs column-names row-contents)]
        {row-name {column-name element}}))))

(defmacro maketable[] '(def table
                         (->
                          (get-string)
    
                          parse-table)))

(def flavorListener (reify java.awt.datatransfer.FlavorListener (^void flavorsChanged [this #^java.awt.datatransfer.FlavorEvent changed] 
                                                                 (doto  (Thread. (Thread/sleep 300) (try (when (.isDataFlavorAvailable clip DataFlavor/stringFlavor) (do 
    (reset! currclip (.getData clip DataFlavor/stringFlavor)))) ( catch Exception e   )  ))))))
;;;clear listener
;(map #(.removeFlavorListener clip %1)  ( .getFlavorListeners clip))
( .addFlavorListener clip ^java.awt.datatransfer.FlavorListener flavorListener )

(defn lazy-file-lines [file]
  (letfn [(helper [rdr]
                  (lazy-seq
                    (if-let [line (.readLine rdr)]
                      (cons line (helper rdr))
                      (do (.close rdr) nil))))]
         (helper (clojure.java.io/reader file :encoding "UTF-8"))))
;You can map, reduce, count, etc. over this lazy sequence:

;(count (lazy-file-lines "/tmp/massive-file.txt"))

(defn raw-multi-split-at[ spvec chvec ]

       (loop [ spvec spvec chvec chvec   ]
       (let [cntspvec (count spvec)
             splited ( split-at (first spvec ) chvec) 
             splitfn (fn [ sp  chvec]())]
       (if ( < (count spvec)  2) splited (cons (first splited) (raw-multi-split-at (rest spvec) (second splited))) )
       )
 )
)
(defn multi-split-at[ spvec chvec ](map #(apply str %) (raw-multi-split-at spvec chvec )))

(defn string-multi-split-at [ spvec chvec   ]
( map (fn [a] ( do (new String (byte-array a) "SJIS"))) (raw-multi-split-at spvec(.getBytes chvec "SJIS")))
  )
(defn split-with-delimiter [s delimiter]
  (let [parts (clojure.string/split s (re-pattern (str "(?=" delimiter ")|(?<=" delimiter ")")))]
    (remove empty? parts)))

(defn file2excel[](let [rg (re-matches #"(.*/)(.*?)$" (get-string)) dir (second rg) file (nth  rg 2) ] (set-string (str dir "\t" file ) ) ))

(defn split-and-ensure-empty [s delimiter]
  (let [split-str (clojure.string/split s delimiter -1)]
    split-str))

(defn split-and-pad [s delimiter length]
  (let [split-str (clojure.string/split s delimiter)]
    (if (< (count split-str) length)
      (concat split-str (repeat (- length (count split-str)) ""))
      split-str)))


(defn merge-tables [table1 table2 column1 column2]
  (let [table2-map (into {} (map #(vector (% column2) %) table2))]
    (for [row1 table1
          :let [value (row1 column1)]
          :when (contains? table2-map value)]
      (concat row1 (table2-map value)))))

(defn merge-tables-with-nil [table1 table2 column1 column2]
  (let [headers1 (first table1)
        headers2 (first table2)
        table2-map (into {} (map #(vector (% column2) %) (rest table2)))
        merged-headers (concat headers1 headers2)]
    (cons merged-headers
          (for [row1 (rest table1)
                :let [key (row1 column1)
                      row2 (get table2-map key [nil nil nil])]]
            (concat row1 row2)))))

(defn merge-maps-with-nil [list1 list2 key1 key2]
  (let [list2-map (into {} (map #(vector (% key2) %) list2))]
    (for [map1 list1
          :let [key-value (map1 key1)
                map2 (get list2-map key-value {:d nil :e nil})]]
      (merge map1 map2))))

;; 使用例
(def list1 [{:a 1 :b "Alice" :c 30}
            {:a 2 :b "Bob" :c 25}
            {:a 3 :b "Charlie" :c 35}])

(def list2 [{:b "Alice" :d "New York" :e "Engineer"}
            {:b "Bob" :d "Los Angeles" :e "Designer"}
            {:b "David" :d "Chicago" :e "Manager"}])

(def result (merge-maps-with-nil list1 list2 :b :b))

;; 結果を表示
(println result)

;; ;; 使用例
;; (def table1 [["a" "b" "c"]
;;              [1 "Alice" 30]
;;              [2 "Bob" 25]
;;              [3 "Charlie" 35]])

;; (def table2 [["b" "d" "e"]
;;              ["Alice" "New York" "Engineer"]
;;              ["Bob" "Los Angeles" "Designer"]
;;              ["David" "Chicago" "Manager"]])

;; (def result (merge-tables-with-nil table1 table2 1 0))

;; ;; 結果を表示
;; (println result)
;; ;; 使用例
;; (def table1 [["a" "b" "c"]
;;              [1 "Alice" 30]
;;              [2 "Bob" 25]
;;              [3 "Charlie" 35]])

;; (def table2 [["b" "d" "e"]
;;              ["Alice" "New York" "Engineer"]
;;              ["Bob" "Los Angeles" "Designer"]
;;              ["David" "Chicago" "Manager"]])

;; (def result (merge-tables (rest table1) (rest table2) 1 0))

;; 結果を表示
;;(println result)

