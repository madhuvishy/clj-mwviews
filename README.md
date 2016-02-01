# clj-mwviews

A Clojure client library for the Wikimedia Pageviews API

## Usage

```
; Article view counts
(article-views "en.wikipedia" ["Barack_Obama" "Apple"] "20150911" "20150913")
(article-views "en.wikipedia" ["Barack_Obama" "Apple"] "20150911" "20150913" :access "mobile app")


; Per-project aggregate view counts
(project-views ["en.wikipedia" "de.wikipedia"] "20150911" "20150913")
(project-views ["en.wikipedia" "de.wikipedia"] "20150911" "20150913" :agent "user")


; Top articles view counts
(top-articles "en.wikipedia" "2015" "09" "10")

```

## API Documentation

[https://wikitech.wikimedia.org/wiki/Analytics/PageviewAPI](Wikitech)
[https://wikimedia.org/api/rest_v1/?doc](Technical docs)

## TODO

* Package library as jar
* Easier methods to access api last-n-days style
* Explore different formats of output like producing a timeseries instead of the regular api json output

