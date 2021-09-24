(ns rest-demo.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [ring.middleware.json :refer :all]
            [clojure.data.json :as json]
            [clojure.string :as str])
  (:gen-class))


; map of jobs 
(def jobs (atom {}))

(defn get-all-jobs [] @jobs)

(defn addjob [company title description]
  (let [id (str (java.util.UUID/randomUUID))]
    (swap! jobs assoc
           id
           {:id id
            :company company
            :title title
            :description description})))

(defn remove-job [id]
  swap! jobs dissoc id)

(println (addjob  "exoscale" "engineer" "do things"))

(defn list-jobs-handler [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (str (json/write-str (get-all-jobs)))})

(defn add-job-handler [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body  (str (json/write-str (addjob
                                (get-in req [:body :company])
                                (get-in req [:body :title])
                                (get-in req [:body :description]))))})
(defn remove-job-handler [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (->
          (println))
   })

(defroutes app-routes
  (GET "/jobs" [] list-jobs-handler)
  (POST "/jobs" [] add-job-handler)
  (DELETE "/jobs/:id" [id] remove-job-handler))

(defn -main
  "Main entry point "
  [& args]
  (let [port (Integer/parseInt "8888")]
    (server/run-server (wrap-json-body #'app-routes {:keywords? true})  {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))


