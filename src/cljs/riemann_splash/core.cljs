(ns riemann-splash.core
  (:require-macros [cljs.core.async.macros :refer [go alts!]])
  (:require [cljs.core.async :refer [>! <! chan]]
            [riemann-splash.server :as server]))

(defn run
  []
  (let [c (server/subscribe "service = \"cpu\"")]
    (go (loop [[t v] (<! c)]
          (condp = t
            :message  (do
                        (.log js/console (clj->js v)))
            nil)
          (recur (<! c))))))







