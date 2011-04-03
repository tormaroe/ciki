(ns ciki.wikipage
    (:use [ciki.templates :as templates]
          ciki.data
          hiccup.core hiccup.form-helpers hiccup.page-helpers))

(defn view-link [id] (link-to (str "/" id)         "View"))
(defn edit-link [id] (link-to (str "/" id "/edit") "Edit"))

(defn compile-markdown [text]
      (.markdownToHtml (org.pegdown.PegDownProcessor.) text))

(defn render-content [page]
      (list (edit-link (page :name)) 
            [:br] 
            (compile-markdown (page :content))))

(defn render-form [id page]
      (let [args (if page
                   { :id          id 
                     :method      :post 
                     :submit-text "Update"
                     :content     (page :content) }
                   { :id          id 
                     :method      :put 
                     :submit-text "Create page"
                     :note        [:div {:style "color:red;"} 
                                        "Page does not yet exist. Create it now:"] })]
        (form-to [(args :method) (str "/" (args :id))]               
                 (hidden-field "wiki-id" (args :id))
                 (args :note)
                 (text-area {:rows 20 :style "width:100%;"}
                            "wiki-content" (args :content))
                 [:br]
                 (submit-button (args :submit-text))
                 (view-link (args :id)))))

(defn render [id displaytype]
      (let [page (fetch-page id)]
        (templates/page id
          (if (and page (= displaytype :view)) 
            (render-content page)
            (render-form id page)))))

(defn render-all []
      (letfn [(pages-2-list [pages]
                            [:ul (map (fn [page]
                                          [:li (link-to (str "/" (:lookup page))
                                                        (:name page))])
                                      pages)])]
             (templates/page "all"
                             (let [pages (sort-by :lookup (all-pages))
                                   page-groups (split-at (/ (count pages) 2) 
                                                         pages)]
                               [:div
                                 [:h2 "All pages"]
                                 [:table {:width "100%"}
                                         [:tr
                                           [:td (pages-2-list (page-groups 0))]
                                           [:td (pages-2-list (page-groups 1))]]]]))))

(defn save [save-fn params]
      (let [id (params "wiki-id")]
        (save-fn id (params "wiki-content"))
        (render id :edit))) 

(defn create [params] (save create-page params))
(defn update [params] (save update-page params))
