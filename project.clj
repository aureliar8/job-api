(defproject rest-demo "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 ; Compojure - A basic routing library
                 [compojure "1.6.1"]
                 ; Our Http library for client/server
                 [http-kit "2.3.0"]
                 [ring/ring-json "0.5.1"]
                 ; Clojure data.JSON library
                 [org.clojure/data.json "0.2.6"]]
  :main ^:skip-aot rest-demo.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
