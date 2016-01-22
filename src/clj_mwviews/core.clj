(ns clj-mwviews.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clj-time.format :as f]))

(def root-url "https://wikimedia.org/api/rest_v1/metrics/pageviews")

(defn get-json [url]
  (json/read-str (:body (client/get url))))

(def hourly (f/formatter "yyyyMMddHH"))
(def daily (f/formatter "yyyyMMdd"))
(def yearly (f/formatter "yyyy"))

(defn format-date [granularity date-string]
  (f/unparse hourly (f/parse (load-string granularity) date-string)))

(defn article-views-url [project access agent article granularity start end]
  (clojure.string/join "/" [root-url "per-article" project access agent article granularity start end]))

(defn format-article-views [lazymap]
  lazymap)

(defn article-views [project articles & {:keys [access agent granularity start end]
                                        :or {access "all-access"
                                             agent "all-agents"
                                             granularity "daily"
                                             start "20150911"
                                             end "20150913"}}]
  (let [start-date (format-date granularity start)
        end-date (format-date granularity end)]
    (format-article-views
      (map #(get-json
             (article-views-url project access agent % granularity start-date end-date))
           articles))))

(article-views "en.wikipedia" ["Barack_Obama" "Apple"])


