(ns bank-ocr.ascii)

;     _  _     _  _  _  _  _
;   | _| _||_||_ |_   ||_||_|
;   ||_  _|  | _||_|  ||_| _|

(defn make-ascii [& v]
  (apply str v))

(def zero
  (make-ascii " _ "
              "| |"
              "|_|"))

(def one
  (make-ascii "   "
              "  |"
              "  |"))

(def two
  (make-ascii " _ "
              " _|"
              "|_ "))

(def three
  (make-ascii " _ "
              " _|"
              " _|"))

(def four
  (make-ascii "   "
              "|_|"
              "  |"))

(def five
  (make-ascii " _ "
              "|_ "
              " _|"))

(def six
  (make-ascii " _ "
              "|_ "
              "|_|"))

(def seven
  (make-ascii " _ "
              "  |"
              "  |"))

(def eight
  (make-ascii " _ "
              "|_|"
              "|_|"))

(def nine
  (make-ascii " _ "
              "|_|"
              " _|"))
