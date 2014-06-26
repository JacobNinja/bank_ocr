(ns bank-ocr.core-test
  (:require [clojure.test :refer :all]
            [bank-ocr.core :refer :all]
            [bank-ocr.ascii :as ascii])
  (:import (java.io BufferedReader StringReader)))

(defn make-ascii [v]
  (apply str (interpose \newline v)))

(defn concat-ascii [& vs]
  (loop [vs vs
         r ""]
    (if (empty? (first vs))
      r
      (recur (map rest vs)
             (str r (apply str (map first vs)) \newline)))))

(deftest bank-ocr-test
  (testing "parse single number"
    (is (= 0 (parse (make-ascii ascii/zero))))
    (is (= 1 (parse (make-ascii ascii/one))))
    (is (= 2 (parse (make-ascii ascii/two))))
    (is (= 3 (parse (make-ascii ascii/three))))
    (is (= 4 (parse (make-ascii ascii/four))))
    (is (= 5 (parse (make-ascii ascii/five))))
    (is (= 6 (parse (make-ascii ascii/six))))
    (is (= 7 (parse (make-ascii ascii/seven))))
    (is (= 8 (parse (make-ascii ascii/eight))))
    (is (= 9 (parse (make-ascii ascii/nine))))
    )
  (testing "parse multiple numbers")
    (is (= 1234567890 (parse (concat-ascii ascii/one ascii/two ascii/three
                                           ascii/four ascii/five ascii/six
                                           ascii/seven ascii/eight ascii/nine ascii/zero ))))
  (testing "validate account number"
    (is (valid-account-number? 457508000))
    (is (= false (valid-account-number? 664371495)))))
