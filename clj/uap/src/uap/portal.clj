(ns uap.portal
  (:require [portal.api :as p]
            [portal.viewer :as v]
            [clojure.core.protocols :refer [Datafiable]])
  (:use [clojure.datafy :refer [datafy]]))
;; for node and jvm
;;(require '[portal.api :as p])

;; for web
;; NOTE: you might need to enable popups for the portal ui to work in the
;; browser.
;;(require '[portal.web :as p])

(declare portal)
(def portal (as-> (p/open) x (add-tap #'p/submit)  x )) ; Open a new inspector
;; (p/eval-str (slurp (clojure.java.io/resource "uap/viewer.cljs")))



;; (require '[clojure.core.protocols :refer [Datafiable]])
 (extend-protocol Datafiable
  java.io.File
  (datafy [^java.io.File this]
    {:name          (.getName this)
     :absolute-path (.getAbsolutePath this)
     :flags         (cond-> #{}
                      (.canRead this)     (conj :read)
                      (.canExecute this)  (conj :execute)
                      (.canWrite this)    (conj :write)
                      (.exists this)      (conj :exists)
                      (.isAbsolute this)  (conj :absolute)
                      (.isFile this)      (conj :file)
                      (.isDirectory this) (conj :directory)
                      (.isHidden this)    (conj :hidden))
     :size          (.length this)
     :last-modified (.lastModified this)
     :uri           (.toURI this)
     :files         (seq (.listFiles this))
     :parentname    (.getAbsolutePath (.getParentFile this))}))

(defn file? [value] (instance? java.io.File value))
;(require '[portal.api :as p])

 ;; (def slides
 ;; ^{::v/default :portal-present.viewer/slides}
 ;; [^{::v/default ::v/hiccup} [:h1 "hello"]
 ;; ^{::v/default ::/hiccup} [:h1 "world"]])

(defn view-file [value]
  (let [datavalue (datafy value)]
    (fn [datavalue]
      [:<> datavalue
       ;[ins/inspector (nth (seq slides) @slide :no-slide)]
       ;[:button {:on-click #(swap! slide dec)} "prev"]
       ;[:button {:on-click #(swap! slide inc)} "next"]
     ])))

;; (portal.(api/register-viewer!
;;  {:name ::file
;;   :predicate file?
;;   :component view-file})

;; Or With an extension installed, do:
;;(def p (p/open {:launcher :vs-code}))  ; jvm / node only
;;(def p (p/open {:launcher :intellij})) ; jvm / node only
;;
;;(add-tap #'portal/submit) ; Add portal as a tap> target
;;
;;(tap> :hello) ; Start tapping out values
;;
;;(p/clear) ; Clear all values
;;
;;(tap> :world) ; Tap out more values
;;
;;(prn @p) ; bring selected value back into repl
;;
;;(remove-tap #'p/submit) ; Remove portal from tap> targetset
;;
;;(p/close) ; Close the inspector when done
;;
;;(p/docs) ;(p View docs locally via Portal - jvm / node only
