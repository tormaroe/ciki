(ns ciki.data
    (:use somnium.congomongo))

(mongo! :db "ciki")

(defn -fix-lookup-name [name]
      (-> name 
          .toLowerCase
          (.replace " " "")))

(defn fetch-page [name]
      (println "* fetching page" name)
      (fetch-one :page :where 
                 {:lookup (-fix-lookup-name name)}))

(defn create-page [name content]
      (println "*** create-page mongo:" name)
      (insert! :page
               {:lookup (-fix-lookup-name name)
                :name name
                :content content}))

(defn update-page [name content]
      (println "*** update-page mongo:" name)
      (let [old-page (fetch-page name)]
        (update! :page old-page
                 (merge old-page 
                        {:name name
                         :content content}))))
