(ns swing-clip.schtasks-parser
  (:require [clojure.string :as str]))

(defn safe-trim [s]
  (if (nil? s) "" (str/trim s)))

(defn parse-task [task-lines]
  (reduce (fn [m line]
            (let [[k v] (str/split line #": " 2)]
              (assoc m (keyword (safe-trim k)) (safe-trim v))))
          {}
          task-lines))

(defn parse-schtasks-output [output]
  (let [lines (str/split-lines output)
        tasks (partition-by #(str/starts-with? % "ホスト名:") lines)]
    (vec (map parse-task tasks))))

;; パース結果
;;(parse-schtasks-output sample-output)
