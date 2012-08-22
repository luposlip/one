(ns ^{:doc "When this library is loaded, a listener function is added
  which will be run when a :form or :greeting event is fired. This
  allows the use of the back button to navigate between views. This is
  accomplished by using library.browser.history to keep track of views
  that have previously been visited, and traversing them when
  navigation events are detected."}
  one.sample.history
  (:require [one.dispatch :as dispatch]
            [one.browser.history :as history]
			[one.logging :as log]))

(def meewee-log (log/get-logger "meewee"))
(log/start-display (log/console-output))

(defn nav-handler
  "Handle navigation events by firing the appropriate view token."
  [{:keys [token navigation?]}]
  (log/info meewee-log (str "nav-handler: " (pr-str token) " - " (pr-str navigation?)))
  (when navigation?
    (dispatch/fire token)))

(def ^{:doc "The global history object for this application."}
  history (history/history nav-handler))

(dispatch/react-to #{:init :form :greeting :welcome}
                   (fn [t _]
					 (log/info meewee-log (str "history react-to, token: " t))
                     (history/set-token history (if (#{:init} t) :form t))))

