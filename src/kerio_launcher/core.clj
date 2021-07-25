(ns kerio-launcher.core
  (:use [clojure.java.shell :as shell]
        [clojure.pprint]
        [clojure.java.io :as io])
  (:import [java.awt.event ActionListener]
           [dorkbox.systemTray SystemTray Separator MenuItem])
  (:gen-class))

(defonce ^:dynamic *icon* nil)

(set! SystemTray/FORCE_TRAY_TYPE dorkbox.systemTray.SystemTray$TrayType/AppIndicator)

(defn changeIcon [icon path]
  (.setImage icon (.getResource System path)))

(defn make-menu-item [icon name action]
  (.add (.getMenu icon) (MenuItem. name action)))

(defn make-menu-separator [icon]
  (.add (.getMenu icon) (Separator.)))

(defn start-kerio []
  (shell/sh "systemctl" "start" "kerio-kvc"))

(defn stop-kerio []
  (shell/sh "systemctl" "stop" "kerio-kvc"))

(defn setupIcon []
  (let [icon (SystemTray/get)
        img-black (io/resource "images/disabled.png")
        img-white (io/resource "images/enabled.png")
        quit-action (proxy [ActionListener] []
                      (actionPerformed [event]
                        (.shutdown icon)
                        (System/exit 0)))
        stop-action (proxy [ActionListener] []
                      (actionPerformed [event]
                        (stop-kerio)
                        (.setStatus icon "Disconnected")
                        (.setImage icon img-black)))
        start-action (proxy [ActionListener] []
                       (actionPerformed [event]
                         (start-kerio)
                         (.setStatus icon "Connected")
                         (.setImage icon img-white)))
        menu-black (make-menu-item icon "Disconnect" stop-action)
        menu-white (make-menu-item icon "Connect" start-action)
        _ (make-menu-separator icon)
        menu-quit (make-menu-item icon "Quit" quit-action)]
    (clojure.pprint/pprint img-black)
    (clojure.pprint/pprint icon)
    (doto icon
      (.setStatus "Disconnected")
      (.setImage img-black))))

(defn start! []
  (when (nil? *icon*)
    (alter-var-root #'*icon*
      (fn [& _]
        (setupIcon)))))

(defn -main [& args]
  (start!))

(comment
  (set! SystemTray/DEBUG true))