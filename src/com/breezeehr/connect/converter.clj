(ns com.breezeehr.connect.converter
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:import (java.io ByteArrayOutputStream)))

(defn read [{:keys [encoding] :as config} data]
  (edn/read-string (io/reader data :encoding (or encoding "UTF-8"))))

(defn write [{:keys [encoding] :as config} schema data]
  (with-open [bos (ByteArrayOutputStream. 1024)]
    (with-open [w (if encoding (io/writer bos :encoding encoding) (io/writer bos))]
      (binding [*print-length* false
                *out* w]
        (pr data)))
    ;;death to efficiency, but easiest way without writing something low-level to encode a stream directly into Kafka
    (.toByteArray bos)))
