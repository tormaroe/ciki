(ns ciki.data
    (:use somnium.congomongo))

(mongo! :db "ciki")

(defn fix-lookup-name [name]
      (-> name 
          .toLowerCase
          (.replace " " "")))

(defn all-pages []
      (fetch :page))

(defn fetch-page [name]
      (fetch-one :page :where 
                 {:lookup (fix-lookup-name name)}))

(defn create-page [name content]
      (insert! :page
               {:lookup (fix-lookup-name name)
                :name name
                :content content}))

(defn update-page [name content]
      (let [old-page (fetch-page name)]
        (update! :page old-page
                 (merge old-page 
                        {:name name
                         :content content}))))
