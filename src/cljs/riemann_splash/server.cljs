(ns riemann-splash.server
  (:require-macros [cljs.core.async.macros :as m :refer [go alts!]])
  (:require [cljs.core.async :refer [chan timeout sliding-buffer put! >!]]
            [goog.events]
            [goog.json]
            [goog.string]
            [goog.net.WebSocket]
            [goog.net.WebSocket.MessageEvent]
            [goog.net.WebSocket.EventType :as Events]))

(defn- url
  [proto host port query]
  (format "%s://%s:%s/index?subscribe=true&query=%s"
          proto
          host
          port 
          (js/encodeURIComponent query)))

(defn- json->clj
  [s]
  (js->clj (goog.json/unsafeParse s)))

(defn subscribe
  ([query]
     (subscribe {} query))
  ([{:keys [host port proto] :or {host "127.0.0.1"
                                  port "5556"
                                  proto "ws"}}
    query]
     (let [ws (goog.net.WebSocket.)
           c  (chan)]
       (goog.events.listen ws Events/OPENED (fn [e] (put! c [:opened e])))
       (goog.events.listen ws Events/CLOSED (fn [e] (put! c [:closed e])))
       (goog.events.listen ws Events/MESSAGE (fn [e] (put! c [:message (json->clj (.-message e))])))
       (goog.events.listen ws Events/ERROR (fn [e] (put! c [:error e])))
       (.open ws (url proto host port query))
       c)))

