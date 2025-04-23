(ns swing-tray.tray
  (:require [ctray.state :refer [*runtime-state]])
  (:import [java.awt AWTException
            Image MenuItem PopupMenu SystemTray TrayIcon event.ActionEvent event.ActionListener
            image.BufferedImage] [java.util ArrayList] [java.io File IOException] [javax.imageio ImageIO]
           [org.imgscalr Scalr] [java.util.prefs Preferences]) (:gen-class))  



(defn tray-supported?
  []
  (. SystemTray isSupported))
(defn make-tray ([]( do
                    (let [tray (@*runtime-state :tray) ](if (nil?  (@*runtime-state :tray))
                                                            (swap! *runtime-state assoc :tray (SystemTray/getSystemTray)))) ; if nil
                    (@*runtime-state :tray)))

                ([tray-icon](doto (@*runtime-state :tray) (.add tray-icon))))

(defn make-tray-menu ([](make-tray-menu (str->img  "./resources/TrayIcon.png")))
                     ([image](let [
                                   popup-menu ( new PopupMenu)
                                   tray-icon ( new TrayIcon image  "trayIcon" popup-menu)]

                                  (do (swap! *runtime-state assoc :popup-menu popup-menu)
                                      (swap! *runtime-state assoc :tray-icon  tray-icon)
                                      popup-menu))))

(defn make-menu-item ([str] ( new MenuItem str)))


(def exit-menu-item  (doto ( new MenuItem "EXIT" )(.addActionListener (reify ActionListener(actionPerformed [this e](do (prn e) (System/exit 0)))))))
(def close-menu-item  (doto ( new MenuItem "EXIT" )(.addActionListener (reify ActionListener(actionPerformed [this e](do (prn e) (System/exit 0)))))))

(defn init[] ( do
                 (swap! *runtime-state assoc
                      :prefs (.node (Preferences/userRoot) "ctray"))
                 (let [tray          (make-tray)
                       default-item  exit-menu-item
                       popup-icon    (make-tray-menu)]

                      (do (.add popup-icon default-item)
                          (.add tray  (@*runtime-state :tray-icon))))))





(defn initConsole[titlename](let [  titlestr (if keyword? (name titlename) titlename)
                                    titlekey (if keyword?  titlename (keyword titlename))
                                    menu_iem ( new MenuItem titlestr)]

                             (prn :exit)))





