(ns bank-ocr.core-test
  (:require [clojure.test :refer :all]
            [bank-ocr.core :refer :all]
            [bank-ocr.ascii :as ascii]))

(defn concat-ascii [& vs]
  (apply str
         (apply interleave
                (map clojure.string/split-lines
                     vs))))

(def messed-up-one
  (ascii/make-ascii "   "
                    " _|"
                    "  |"))

(def messed-up-zero
  (ascii/make-ascii "   "
                    "| |"
                    "|_|"))

(deftest bank-ocr-test
  (testing "parse single number"
    (is (= "0" (parse ascii/zero)))
    (is (= "1" (parse ascii/one)))
    (is (= "2" (parse ascii/two)))
    (is (= "3" (parse ascii/three)))
    (is (= "4" (parse ascii/four)))
    (is (= "5" (parse ascii/five)))
    (is (= "6" (parse ascii/six)))
    (is (= "7" (parse ascii/seven)))
    (is (= "8" (parse ascii/eight)))
    (is (= "9" (parse ascii/nine)))
    )
  (testing "parse multiple numbers")
    (is (= "1234567890" (parse (concat-ascii ascii/one ascii/two ascii/three
                                           ascii/four ascii/five ascii/six
                                           ascii/seven ascii/eight ascii/nine ascii/zero))))

  (testing "validate account number"
    (is (valid-account-number? "457508000"))
    (is (= false (valid-account-number? "664371495"))))

  (testing "find valid matches for invalid number"
    (is (= "711111111" (parse-and-match (apply concat-ascii (repeat 9 ascii/one)))))
    (is (= "777777177" (parse-and-match (apply concat-ascii (repeat 9 ascii/seven)))))
    (is (= "200800000" (parse-and-match (apply (partial concat-ascii ascii/two)
                                             (repeat 8 ascii/zero)))))
    (is (= "333393333" (parse-and-match (apply concat-ascii (repeat 9 ascii/three)))))
    (is (= ["888886888" "888888880" "888888988"] (sort (parse-and-match (apply concat-ascii (repeat 9 ascii/eight))))))
    (is (= ["555655555" "559555555"] (sort (parse-and-match (apply concat-ascii (repeat 9 ascii/five))))))
    (is (= ["666566666" "686666666"] (sort (parse-and-match (apply concat-ascii (repeat 9 ascii/six))))))
    (is (= ["899999999" "993999999" "999959999"] (sort (parse-and-match (apply concat-ascii (repeat 9 ascii/nine))))))
    (is (= ["490067115" "490067719" "490867715"] (sort (parse-and-match (concat-ascii ascii/four
                                                                                ascii/nine
                                                                                ascii/zero
                                                                                ascii/zero
                                                                                ascii/six
                                                                                ascii/seven
                                                                                ascii/seven
                                                                                ascii/one
                                                                                ascii/five)))))
    (is (= "123456789" (parse-and-match (concat-ascii messed-up-one
                                                    ascii/two
                                                    ascii/three
                                                    ascii/four
                                                    ascii/five
                                                    ascii/six
                                                    ascii/seven
                                                    ascii/eight
                                                    ascii/nine))))
    (is (= "000000051" (parse-and-match (concat-ascii ascii/zero
                                                    messed-up-zero
                                                    ascii/zero
                                                    ascii/zero
                                                    ascii/zero
                                                    ascii/zero
                                                    ascii/zero
                                                    ascii/five
                                                    ascii/one))))))
