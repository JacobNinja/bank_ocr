(ns bank-ocr.core
  (:require [bank-ocr.ascii :as ascii]
            [clojure.string :as str]))

(def number-length 3)
(def number-depth 3)

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

(defn- extract-numbers [s]
  (map str/join
       (partition number-depth
                  (map str/join
                       (apply interleave
                              (map #(partition number-length %)
                                   (str/split-lines s)))))))

(defn- replace-at [n s replacement]
  (str (str/join (take n s))
       replacement
       (str/join (drop (inc n) s))))

(defn- number-permutations [number]
  (loop [i 0
         perms (list number)]
    (if (>= i (count number))
      perms
      (recur (inc i)
             (condp = (nth number i)
               \space (concat perms (list (replace-at i number "|")
                                          (replace-at i number "_")))
               \_ (concat perms (list (replace-at i number " ")
                                      (replace-at i number "|")))
               \| (concat perms (list (replace-at i number " ")
                                      (replace-at i number "_"))))))))

(defn- ascii-permutations [ascii-numbers]
  (let [numbers (map ascii->number ascii-numbers)]
    (loop [n ascii-numbers
           i 0
           perms '()]
      (if (empty? n)
        perms
        (recur (rest n)
               (inc i)
               (concat perms
                       (map #(replace-at i numbers %)
                            (keep ascii->number
                                  (number-permutations (first n))))))))))

(defn valid-account-number? [n]
  (and (= (count (str n)) 9)
       (let [[d9 d8 d7 d6 d5 d4 d3 d2 d1]
             (map #(Integer/parseInt (str %)) n)
             checksum (+ d1
                         (* 2 d2)
                         (* 3 d3)
                         (* 4 d4)
                         (* 5 d5)
                         (* 6 d6)
                         (* 7 d7)
                         (* 8 d8)
                         (* 9 d9))]
         (= 0 (mod checksum 11)))))

(defn parse [s]
  (str/join (map ascii->number
                 (extract-numbers s))))

(defn parse-and-match [s]
  (let [result (parse s)]
    (if (valid-account-number? result)
      result
      (let [numbers (extract-numbers s)
            result (filter valid-account-number?
                           (ascii-permutations numbers))]
        (if (= (count result) 1)
          (first result)
          result)))))
