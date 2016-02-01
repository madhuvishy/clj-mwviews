(ns clj-mwviews.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clj-time.format :as f]
            [clj-time.core :as t]))

(def root-url "https://wikimedia.org/api/rest_v1/metrics/pageviews")

(defn get-json [url]
  (json/read-str (:body (client/get url))))

; Datetime helpers
(def hourly (f/formatter "yyyyMMddHH"))
(def daily (f/formatter "yyyyMMdd"))
(def yearly (f/formatter "yyyy"))

(defn format-date [granularity date-string]
  (f/unparse hourly (f/parse (load-string granularity) date-string)))

; Construct the API urls for the different endpoints
(defn article-views-url [project access agent article granularity start end]
  (clojure.string/join "/" [root-url "per-article" project access agent article granularity start end]))

(defn project-views-url [project access agent granularity start end]
  (clojure.string/join "/" [root-url "aggregate" project access agent granularity start end]))

(defn top-views-url [project access year month day]
  (clojure.string/join "/" [root-url "top" project access year month day]))


; These format methods are doing nothing right now - they are just placeholders
; to replace with a different format for results.
(defn format-article-views [results]
  results)

(defn format-project-views [results]
  results)

(defn format-top-views [results]
  results)


(defn article-views
  "Get view counts for given project and list of articles"
  ([project articles start end & {:keys [access agent granularity]
                                  :or   {access      "all-access"
                                         agent       "all-agents"
                                         granularity "daily"}}]
   (let [start-date (format-date granularity start)
         end-date (format-date granularity end)]
     (format-article-views
       (map #(get-json
              (article-views-url project access agent % granularity start-date end-date))
            articles)))))


(defn project-views [projects start end & {:keys [access agent granularity]
                                           :or {access "all-access"
                                                agent "all-agents"
                                                granularity "daily"}}]
  "Get aggregate view counts for given project"
  (let [start-date (format-date granularity start)
        end-date (format-date granularity end)]
    (format-project-views
      (map #(get-json
             (project-views-url % access agent granularity start-date end-date))
           projects))))

(defn top-articles [project year month day & {:keys [access]
                                              :or {access "all-access"}}]
  "Get view counts for most viewed articles over a day"
  (format-top-views
    (get-json (top-views-url project access year month day))))
