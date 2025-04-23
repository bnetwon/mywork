(ns user
  (:require [clojure.tools.namespace.repl :as tnr]
            [clojure.repl]
            ;[proto-repl.saved-values]
            [clojure.reflect :as cr]
            [clojure.pprint :as pp]
            [seesaw.core]
            [seesaw.mig]
            [seesaw.table])


  (:import [java.awt Toolkit] [java.awt.datatransfer Clipboard DataFlavor StringSelection])
  (:use [seesaw core table][clojure.repl]))

(defn ins->map[obj](->> obj
                    cr/reflect
                    :members (map #( (fn[x] ( into {}  x)) %)) vec))

(def members-c-n [{:key :name, :text "name", :class java.lang.String}
                  {:key :type, :text "type", :class java.lang.Object}
                  {:key :return-type, :text "return-type", :class java.lang.Object}
                  {:key :declaring-class, :text "declaring-class", :class java.lang.Class}
                  {:key :parameter-types, :text "parameter-types", :class java.lang.Object}
                  {:key :exception-types, :text "exception-types", :class java.lang.Class}
                  {:key :flags, :text "flags", :class java.lang.Object}])

(defn ct-model[obj ](seesaw.table/table-model
                     :columns members-c-n
                     :rows  obj))

(defn center-frame [center]
  (frame :title "JTable Example" :width 500 :height 400 :content
    (border-panel
      :center (scrollable center)
      :south  (label :id :sel :text "Selection: "))))

(require 'clojure.tools.namespace.repl)
(import 'org.pfsw.joi.Inspector)

(defn reset []
  (tnr/refresh :after 'user/start))

(println "proto-repl-demo dev/user.clj loaded.")

(defn ^javax.swing.table.DefaultTableCellRenderer default-table-cell-renderer
  [render-fn]
  (if (instance? javax.swing.table.DefaultTableCellRenderer render-fn)
    render-fn
    (proxy [javax.swing.table.DefaultTableCellRenderer] []
      (^java.awt.Component getTableCellRendererComponent [^javax.swing.JTable table, ^Object value,
                                                          ^Boolean selected?, ^Boolean focus?, ^Integer row, ^Integer column]
                (let [^javax.swing.table.DefaultTableCellRenderer this this]
                 (proxy-super getTableCellRendererComponent table value selected? focus? row column)
                ;(proxy-super getListCellRendererComponent component value index selected? focus?)
                 (render-fn this { :this      this
                                   :component table
                                   :value     value
                                   :selected? selected?
                                   :focus?    focus?
                                   :row       row
                                   :column    column})
                 (apply config! this [:background "#aaaaee"]))))));(todo this (.setBackgroundColor "#000033") ))
                ; int columnModelIndex = table.getColumnModel().getColumn(column).getModelIndex();

(defn omap[obj]
  (let[ obj-map    (->> obj clojure.reflect/reflect :members
                        (map #( (fn[x] ( into {}  x)) %)) vec)]
    obj-map))

(defn tablemap[obj]
  (let[ obj-map    (->> obj clojure.reflect/reflect :members
                        (map #( (fn[x] ( into {}  x)) %)) vec)
        tablemodel (seesaw.table/table-model
                    :columns members-c-n
                    :rows  obj-map)
        table      (table :model tablemodel)]

   (-> {} (assoc :obj-map    obj-map)
          (assoc :tablemodel tablemodel)
          (assoc :table      table))))

(defn tableframemap[obj]
  (let[ tablemap   (tablemap obj)
        cframe     (center-frame (:table tablemap))
        cfok       (show! cframe)]
    (-> tablemap (assoc :frame cframe))))
;
; (def mi (seesaw.mig.mig-panel
;           :constraints ["wrap 2"
;                         "[shrink 0]20px[200, grow, fill]"
;                         "[shrink 0]5px[]"]
;           :items [ ["name:"     ] [(text (or "name"     ""))]
;                    ["category:" ] [(text (or "category" ""))]
;                    ["date:"     ] [(text (or "date"     ""))]
;                    ["comment:"  ] [(text (or "comment"  ""))]]))
; (def fm (let [inum (atom 0)]{:cntup (fn[](swap! inum inc))
;                              :get   (fn[]@inum)
;                              :geti  (identity @inum)
;                              :atm   inum}))
;
(defn start
  []
  ; (println "I'm starting now")
  (println "Start completed"))
