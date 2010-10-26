(ns ciki.core
    (:require ciki.wikipage)           
    (:use compojure.core ring.util.response ring.adapter.jetty))

(defroutes ciki-routes 
  (GET  "/"         [  ] (redirect "/wiki/index"))
  (GET  "/wiki/:id" [id] (ciki.wikipage/render id :view))
  (GET  "/edit/:id" [id] (ciki.wikipage/render id :edit))
  (PUT  "/wiki" {params :params} (ciki.wikipage/create params))
  (POST "/wiki" {params :params} (ciki.wikipage/update params)))

(defonce run (run-jetty ciki-routes { :port 8083 :join? false }))
