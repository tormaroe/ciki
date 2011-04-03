(ns ciki.templates
    (:use hiccup.core hiccup.form-helpers hiccup.page-helpers))

(def silver-line "solid 1px silver;")

(defn searchbox-value []
      "document.getElementById('wiki').value")

(defn ciki-redirect [wiki]
      (str "document.location = '/' + " wiki ";"))

(defn header [pagetitle]
      [:div#header {:style (str "border-bottom: " silver-line
                                "padding-bottom:2px;")}
                   [:div#search {:style "float:right;"}
                                (link-to "/all" "All pages")
                                (text-field "wiki")
                                (submit-button 
                                  {:onClick (ciki-redirect (searchbox-value))} 
                                  "GO")]
                   [:span {:style "font-size:14pt;font-weight:bold;"} 
                          (link-to "/" "ciki")
                          " &gt; "
                          (link-to (str "/" pagetitle) pagetitle)]])

(defn footer []
      [:div#footer {:style (str "border-top: " silver-line
                                "text-align:right;")} 
                   "ciki"])

(defn page [title & markup]
      (html [:html 
              [:head 
                [:title (str "ciki : " title)]
                [:style {:type "text/css"}
                        (str "body {"
	"font: 10pt 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, Sans-Serif;"
  "}")]]
              [:body
                (header title)
                [:div#main markup]    
                (footer)]]))
