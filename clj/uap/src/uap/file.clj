(ns uap.file
  (:require [clojure.java.io :as io]))

(defn read-lines [file-path]
  (with-open [reader (io/reader file-path)] 
 (doall (line-seq reader))))

(defn read-lines-act [file-path act]
  (with-open [reader (io/reader file-path)] 
 (act (line-seq reader))))

(defn write-lines [file-path lines]
  (with-open [writer (io/writer file-path)]
   (doseq [line lines]
   (.write writer (str line "\n")))))


(defn read-lines-batch [file-path batch-size]
  (with-open [reader (io/reader file-path)]
    (loop [lines []]
      (let [line (line-seq reader)]
        (if (empty? line)
          lines
          (recur (concat lines (take batch-size line))))))))


(defn process-file [input-file output-file]
   (let [lines (read-lines input-file)]
   (write-lines output-file lines)))

(defn process-file-act [input-file output-file act]
   (let [lines (read-lines input-file)]
   (write-lines output-file lines)))

(defn process-file-in-batches [input-file output-file batch-size]
  (let [lines (read-lines-batch input-file batch-size)]
    (write-lines output-file lines)))

;; Žg—p—á
;;(process-file "input.txt" "output.txt")
;;(process-file-in-batches "input.txt" "output.txt" 100)
