(ns bank-ocr.core
  (:require [bank-ocr.ascii :as ascii])
  (:import (java.io BufferedReader StringReader)))

(def number-length 4)

(def ascii->number
  {ascii/zero 0
   ascii/one 1
   ascii/two 2
   ascii/three 3
   ascii/four 4
   ascii/five 5
   ascii/six 6
   ascii/seven 7
   ascii/eight 8
   ascii/nine 9})

(defn parse [s]
  (let [lines (map #(partition number-length %)
                   (line-seq (BufferedReader. (StringReader. s))))
        numbers (partition (count lines) (apply interleave lines))]
    (->> (map (fn [ascii]
                (ascii->number (vec (map #(apply str %)
                                         ascii))))
              numbers)
         (apply str)
         (Integer/parseInt))))

(defn valid-account-number? [n]
  (let [[d9 d8 d7 d6 d5 d4 d3 d2 d1] (map #(Integer/parseInt (str %)) (str n))
        checksum (+ d1
                    (* 2 d2)
                    (* 3 d3)
                    (* 4 d4)
                    (* 5 d5)
                    (* 6 d6)
                    (* 7 d7)
                    (* 8 d8)
                    (* 9 d9))]
    (= 0 (mod checksum 11))))
