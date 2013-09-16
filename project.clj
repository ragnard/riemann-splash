(defproject riemann-splash "0.1.0-SNAPSHOT"
  :description "blahonga"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"sonatype-oss"
                 "https://oss.sonatype.org/content/groups/public/"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 ;; [org.clojure/core.async "0.1.222.0-83d0c2-alpha"]
                 [org.clojure/clojurescript "0.0-1844"]
                 [prismatic/dommy "0.1.1"]]

  :plugins [[lein-cljsbuild "0.3.2"]]
  :source-paths ["src/clj"]
  :cljsbuild
  {:builds [{:notify-command ["terminal-notifier" "-title" "lein-cljsbuild" "-message"]
             :source-paths ["src/cljs"]
             :compiler
             {:libs ["src/js"]
              :output-to "resources/public/javascripts/splash.js"
              :optimizations :whitespace
              :warnings true
              :pretty-print true}}]})
