(ns ciki.core
    (:require ciki.wikipage)           
    (:use compojure.core ring.util.response ring.adapter.jetty))

(defroutes ciki-routes 
  (GET  "/"         [  ] (redirect "/index"))
  (GET  "/:id"      [id] (ciki.wikipage/render id :view))
  (GET  "/:id/edit" [id] (ciki.wikipage/render id :edit))
  (PUT  "/:id" {params :params} (ciki.wikipage/create params))
  (POST "/:id" {params :params} (ciki.wikipage/update params)))

(defonce run (run-jetty ciki-routes { :port 8083 :join? false }))
