(ns uap.core
(:require
    [ss.loop :as ssl]
    [clojure.core.async :as a]
    [java-time.api :as jt]
    [swing-clip.clipboard])
  (:import [java.awt Toolkit]
           [java.io File]
           [java.awt.datatransfer Clipboard DataFlavor ClipboardOwner Transferable StringSelection]))


;(require 'clojure.tools.namespace.repl)
(def ^Clipboard clip swing-clip.clipboard/clip)
(use 'ctray.core)
;(require '[clojure.core.async :as async])

(def filechan (atom (a/chan (clojure.core.async/sliding-buffer 1))))
(def stringchan (atom (a/chan (clojure.core.async/sliding-buffer 1))))

(def lastString (atom nil))
(def lastFile (atom nil))

(defn getLastString [] (let [ls (a/poll! @stringchan)] (
if ls (reset! lastString ls) @lastString ) ))
(defn getLastFile [] (let [ls (a/poll! @filechan)] (
if ls (reset! lastFile ls) @lastFile )))

(def clipsleep (atom 250))
;(def labelStr "') ")

;(def fileaction (atom (fn [filee] (prn filee ))))


(def afn
  (atom
   (fn
     [^ClipboardOwner owner
      ^Transferable contents
      ^Clipboard c
      ^Transferable t]
     (if contents
       (cond  (. contents isDataFlavorSupported DataFlavor/javaFileListFlavor)
              (clojure.core.async/go (clojure.core.async/>!! @filechan (. contents getTransferData DataFlavor/javaFileListFlavor)))
              (. contents isDataFlavorSupported DataFlavor/stringFlavor)
              (clojure.core.async/go (clojure.core.async/>!! @stringchan (. contents getTransferData DataFlavor/stringFlavor)))
              :else nil)))))

(defn resetafn[]
  (reset! afn
    (fn
      [^ClipboardOwner owner
       ^Transferable contents
       ^Clipboard c
       ^Transferable t]
     (if contents
      (cond  (. contents isDataFlavorSupported DataFlavor/javaFileListFlavor)
             (prn (. contents getTransferData DataFlavor/javaFileListFlavor))
             (. contents isDataFlavorSupported DataFlavor/stringFlavor)
             (prn (. contents getTransferData DataFlavor/stringFlavor))
             :else nil)))))

(def clipOwner (reify java.awt.datatransfer.ClipboardOwner
                 (^void  lostOwnership [this ^Clipboard c ^Transferable t]
                   (do (Thread/sleep @clipsleep)
                       (let [^Transferable contents (. c getContents this)]
                         (do
                           (@afn this contents c t)
                        ; (. c setContents contents this)
                           ))))))
;
(def nilOwner (reify java.awt.datatransfer.ClipboardOwner
                (^void  lostOwnership [this ^Clipboard c ^Transferable t]
                  nil)))

(defn ownerInit ([] (do (let [^Transferable contents (. clip getContents clipOwner)]
                       (. clip setContents contents clipOwner))))
                ([cO ] (do (let [^Transferable contents (. clip getContents clipOwner)]
                       (. clip setContents contents cO)))))


(def helpStr "swing-clip.clipboard,swing-clip.sql")
(def helpStrS "(use '[swing-clip.clipboard] '[swing-clip.sql])")

(defmacro def- [item value]
  `(def ^{:private true} ~item ~value)
)
(defn -main [& args]
  "start"
  (println args))
;
;(defn split-data 
; ([ tstr b ] 
;  (list (apply str (take b tstr))
;        (apply str (drop b tstr)) )
;  )
; ([ tmplist tmpstr b & colls ] 
;   ( if (seqable? tmplist)
;   ( let [encoding "SJIS"
;          bstr (. tmpstr getBytes  encoding)
;          restcoll (if (seq? (first colls)) (flatten colls) colls )
;          cc (count restcoll) ] 
;    (if (not (empty? restcoll))
;      (let [fstr (new String (byte-array (take b bstr)) encoding)  ]
;       (-> tmplist 
;         (conj fstr ) 
;         (into (split-data tmplist (new String  (byte-array (drop b bstr ))  encoding ) (first restcoll) (rest restcoll))))
;      )
;    ;ELSE
;      (into tmplist (split-data tmpstr b))
;    )
;   )
;   ;not list
;   (split-data []  tmplist tmpstr b colls )
; )))
; 
;;(defn split-bytes ([ str b] (
;; if (instance? String str)
;; (let [bstr (. str getBytes  "SJIS") ]( list))
;; (new String bstr 0 b "SJIS" )   (new String bstr b (- (count bstr) b )  "SJIS")
;;
;; ((take b str) (drop b str))
;;
;; ([ str b & colls] ())
;; let [cc (count colls)]
;; (if (> cc 1)
;;    (let [sbs (split-bytes   str b) ]( list))
;;   (take 1 sbs ) (do (split-bytes (drop 1 sbs) (take 1 colls) (drop 1 colls)))))))
;;
;;    (split-bytes  str b)))
;;;(defn split-bytes ([ stra b] (
;;; if (instance? String stra)
;;; (let [bstra (. stra getBytes  "SJIS") ]
;;; (list (new String bstra 0 b "SJIS" )   (new String bstra b (- (count bstra) b )  "SJIS")))
;;;
;;; ((take b stra) (drop b stra))))
;;;
;;; ([ stra b & colls] 
;;;  ( let [cc (count colls)]
;;;   (if (> cc 0)
;;;    (let [sbs (. stra getBytes  "SJIS") 
;;;          bbb (prn b)
;;;          sss (prn "aa")
;;;          ss  (prn  (new String sbs 0 b "SJIS" ))  ]
;;;     (list (new String sbs 0 b "SJIS" ) 
;;;       (split-bytes (new String  sbs b (- (count sbs) 1) "SJIS") (take 1 colls) (drop 1 colls))) )
;;;    (split-bytes  stra b)))))
;;;
;;;;(take 1 sbs )
;
;
;
;;(require 'uap.clip-sample)
;
;;(split-bytes "abcde" 1 1 1 1)
(use 'swing-clip.clipboard)
(use 'swing-clip.sql)
(defn testa [](ssl/go-loop
                  ^{:id 300}
                  [i 0]
                  (println i)
                  (a/<! (a/timeout 5000))
                  (recur (inc i)))
)
(defn initfilewatch [] (ssl/go-loop
                         ^{:id 301}
                         [tmpfile nil]
                          (do (if tmpfile (reset! lastFile tmpfile))
                              (recur (a/<!! @filechan)))))
(defn initstringwatch [] (ssl/go-loop
                         ^{:id 302}
                         [tmpstring nil]
                          (do (if tmpstring (reset! lastString tmpstring))
                              (recur (a/<!! @stringchan)))))
;;(use 'swing-clip.sqldb)
;(require 'inspector.core)
;(ssl/stop 42)
(defn actfn [mapflg] (
  if (:act mapflg) (do (prn "")) (Thread/sleep 1000)))

(defn dynamic-record-conversion [target-record source-record]
  (let [target-fields (keys target-record)
        source-fields (keys source-record)
        common-fields (filter #(some #{%} source-fields) target-fields)]
    (reduce (fn [acc field]
              (assoc acc field (get source-record field nil)))
            (into {} (map (fn [field] [field nil]) target-fields))
            common-fields)))




