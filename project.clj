(defproject gs-kerio-launcher "0.1.0-SNAPSHOT"
  :description "Simple graphical launcher for Kerio VPN client"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.dorkbox/SystemTray "4.1"]]
  :main gs-kerio-launcher.core
  :repl-options {:init-ns gs-kerio-launcher.core}
  :profiles {
             :uberjar {
                       :aot [gs-kerio-launcher.core]
                       :uberjar-name "gs-kerio-launcher.jar"
                       }
             })
