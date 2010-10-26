(ns ciki.wikipage
    (:use [ciki.templates :as templates]
          ciki.data
          hiccup.core hiccup.form-helpers hiccup.page-helpers))

(defn view-link [id] (link-to (str "/wiki/" id) "View"))
(defn edit-link [id] (link-to (str "/edit/" id) "Edit"))

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
        (form-to [(args :method) "/wiki"]               
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

(defn save [save-fn params]
      (let [id (params "wiki-id")]
        (save-fn id (params "wiki-content"))
        (render id :edit))) 

(defn create [params] (save create-page params))
(defn update [params] (save update-page params))
