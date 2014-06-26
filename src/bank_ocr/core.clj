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
