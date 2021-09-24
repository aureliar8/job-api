(ns rest-demo.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [ring.middleware.json :refer :all]
            [clojure.data.json :as json]
            [clojure.string :as str])
  (:gen-class))

;; Data manipulation. Should likely be in it's own
;; file/package/module/namespace 

;; In memory job storage via a map where the key is the job uuid 
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
  (swap! jobs dissoc id))

(addjob  "exoscale" "engineer" "do things")


;;-------------------------------------------------------------------
;; Api handlers

;; Returns a json map of all jobs 
(defn list-jobs-handler [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (str (json/write-str (get-all-jobs)))})

;; Add a job with the given company, title and description and returns
;; the new map. If one of those field isn't set, it will be
;; inserted with value null. Maybe validate the input and return an
;; error
(defn add-job-handler [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body  (str (json/write-str (addjob
                                (str (get-in req [:body :company]))
                                (str (get-in req [:body :title]))
                                (str (get-in req [:body :description])))))})

;; Remove a job with the given id and return the new map. No error is
;; return if no job with such id exists. 
(defn remove-job-handler [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body  (str (json/write-str (remove-job (get-in req [:params :id]))))})

(defroutes app-routes
  (GET "/jobs" [] list-jobs-handler)
  (POST "/jobs" [] add-job-handler)
  (DELETE "/jobs/:id" [] remove-job-handler)) ;;Todo: ensure that :id has a valid format 

(defn -main
  "Main entry point "
  [& args]
  (let [port (Integer/parseInt "8888")]
    (server/run-server (wrap-json-body #'app-routes {:keywords? true})  {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))


