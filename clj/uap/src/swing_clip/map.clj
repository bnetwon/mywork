(ns swing-clip.map
  (:require [clojure.string :as str]))


(defn expand-map [m]
  (let [keys (keys m)
        vals (vals m)
        lists (filter #(sequential? %) vals)
        non-lists (filter #(not (sequential? %)) vals)]
    (if (empty? lists)
      [m]
      (for [v (first lists)]
        (merge (zipmap keys non-lists) {:c v})))))

;;(def my-map {:a 1 :b 2 :c [5 7]})
;;(expand-map my-map)
(defn combine-maps [maps keys-to-combine]
  (let [grouped (apply merge-with (fn [a b] (if (sequential? a) (conj a b) [a b])) maps)]
    (into {} (map (fn [[k v]]
                    [k (if (contains? (set keys-to-combine) k)
                         v
                         (if (sequential? v) (first v) v))])
                  grouped))))

;;(def maps [{:a 1 :b 2 :c 5} {:a 1 :b 2 :c 7}])
;;(def keys-to-combine [:c])
;;(combine-maps maps keys-to-combine)

(defn test-combine-maps [maps]
  (let [keys (keys (first maps))
        grouped (apply merge-with conj (map #(into {} (map (fn [[k v]] [k [v]]) %)) maps))]
    (into {} (map (fn [[k v]] [k (if (= (count v) 1) (first v) v)]) grouped))))

;;(def maps [{:a 1 :b 2 :c 5} {:a 1 :b 2 :c 7}])
;;(combine-maps maps)

(defn map-to-tab-string [m]
  (->> m
       (map (fn [[k v]] (str (name k) "\t" v)))
       (clojure.string/join "\t")))


(defn maps-list-to-tab-strings [maps]
  (let [keys (map name (keys (first maps)))
        header (clojure.string/join "\t" keys)
        values (map map-to-tab-string maps)]
    (cons header values)))

(defn convert-to-map-list [data]
  (let [columns (first data)
        rows (rest data)]
    (map #(zipmap columns %) rows)))



(defn merge-tables [table1 table2 column1 column2]
  (let [table2-map (into {} (map #(vector (% column2) %) table2))]
    (for [row1 table1
          :let [value (row1 column1)]
          :when (contains? table2-map value)]
      (concat row1 (table2-map value)))) )

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
  (let [all-keys (set (concat (mapcat keys list1) (mapcat keys list2)))
        list2-map (into {} (map #(vector (% key2) %) list2))]
    (for [map1 list1
          :let [key-value (map1 key1)
                map2 (get list2-map key-value {})]]
      (into {} (map (fn [k] [k (or (map1 k) (map2 k) nil)]) all-keys)))))

(defn maps-to-tabbed-string [maps]
  (let [keys (->> maps
                  (mapcat keys)
                  set
                  vec)
        header (clojure.string/join "\t" keys)
        rows (map (fn [m]
                    (clojure.string/join "\t"
                                         (map (fn [k] (get m k ""))
                                              keys)))
                  maps)]
    (clojure.string/join "\n" (cons header rows))))

;; 使用例
(def maps [{:a 1 :b 2}
           {:a 3 :c 4}
           {:b 5 :c 6}])


(defn escape-problematic-chars [value]
  (if (string? value)
    (str/replace value "\"" "\"\"")
    value))

(defn preprocess-data [data]
  (map (fn [record]
         (into {} (map (fn [[k v]] [k (escape-problematic-chars v)]) record)))
       data))

(defn map-list-to-tab-separated-string ([data]
                                        (let [cleaned-data (preprocess-data data)
                                              keys (keys (first cleaned-data))
                                              rows (map #(map (fn [k] (or (get % k) "")) keys) cleaned-data)
                                              header (str/join "\t" keys)
                                              body (str/join "\n" (map #(str/join "\t" %) rows))]
                                          (str header "\n" body)))
([data target-char replacement-char]
 (let [cleaned-data (preprocess-data data target-char replacement-char)
       keys (keys (first cleaned-data))
       rows (map #(map (fn [k] (or (get % k) "")) keys) cleaned-data)
       header (str/join "\t" keys)
       body (str/join "\n" (map #(str/join "\t" %) rows))]
   (str header "\n" body)))
)



;; (def list1 [["A" "B" "C"]
;;             [1 2 3]
;;             [2 3 4]])

;; (def list2 [["F" "G" "H"]
;;             [1 6 7]
;;             [2 8 9]])

;; マージ処理
(defn merge-lists [list1 list2 b-col f-col]
  (let [header1 (first list1)
        header2 (first list2)
        data1 (rest list1)
        data2 (rest list2)
        key-index1 (.indexOf header1 b-col)
        key-index2 (.indexOf header2 f-col)
        data2-map (into {} (map #(vector (nth % key-index2) %) data2))
        nil-row (vec (repeat (count (rest header2)) nil))]
    (cons (concat header1 (rest header2))
          (map (fn [row1]
                 (let [key (nth row1 key-index1)
                       row2 (get data2-map key nil-row)]
                   (concat row1 (rest row2))))
               data1))))

;; マージされたリスト
;; (def merged-list (merge-lists list1 list2 "B" "F"))
;;(println (maps-to-tabbed-string maps))
;; マップのリストの定義
;; (def list1 [{:A 1 :B 2 :C 3}
;;             {:A 2 :B 3 :C 4}])

;; (def list2 [{:F 5 :G 6 :H 7}
;;             {:F 9 :G 8 :H 9}])

;; マップのリストを文字列に変換する汎用的な関数
(defn maps-to-string
  "Converts a list of maps to a string with specified columns, delimiter, and default value for missing keys.
  - maps: List of maps to be converted.
  - columns: Vector of keys specifying the order of columns.
  - delimiter: String used to separate values (default is comma).
  - default-val: Value to use for missing keys (default is 'nil')."
  [maps columns & {:keys [delimiter default-val] :or {delimiter "," default-val "nil"}}]
  (let [header (clojure.string/join delimiter (map name columns))
        rows (map (fn [m]
                    (clojure.string/join delimiter (map #(get m % default-val) columns)))
                  maps)]
    (clojure.string/join "\n" (cons header rows))))

;; マージ処理
(defn merge-maps [list1 list2 b-key f-key]
  (let [data2-map (into {} (map #(vector (f-key %) %) list2))
        all-keys (set (mapcat keys list2))]
    (map (fn [m1]
           (let [key (b-key m1)
                 m2 (get data2-map key (zipmap all-keys (repeat nil)))]
             (merge m1 m2)))
         list1)))

;; マージされたリスト
;; (def merged-list (merge-maps list1 list2 :B :F))

;; 結果の表示
;; (println merged-list)


;; ;; 使用例
;; (def list1 [{:a 1 :b "Alice" :c 30}
;;             {:a 2 :b "Bob" :c 25}
;;             {:a 3 :b "Charlie" :c 35}])

;; (def list2 [{:b "Alice" :d "New York" :e "Engineer"}
;;             {:b "Bob" :d "Los Angeles" :e "Designer"}
;;             {:b "David" :d "Chicago" :e "Manager"}])

;; (def result (merge-maps-with-nil list1 list2 :b :b))

;; 結果を表示
;;(println result)



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

