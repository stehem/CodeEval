(ns davyncy.core-test
  (:require [clojure.test :refer :all]
            [davyncy.core :refer :all]))

(deftest overlap-test
  (is (= '("D" "E" "F")  (find-overlap "ABCDEF" "DEFG")))
  (is (= '("A" "B" "C")  (find-overlap "ABCDEF" "XYZABC")))
  (is (= '("B" "C" "D" "E")  (find-overlap "ABCDEF" "BCDE")))
  (is (= nil  (find-overlap "ABCDEF" "XCDEZ"))))

(deftest match-at-test
  (is (= true (match-at-start (split-by-char "m quaerat voluptatem.") (split-by-char "m quaerat"))))
  (is (= false (match-at-start (split-by-char "m quaerat voluptatem.") (split-by-char "xm quaerat"))))
  (is (= true (match-at-end (split-by-char "m quaerat voluptatem.") (split-by-char "aerat voluptatem."))))
  (is (= false (match-at-end (split-by-char "m quaerat voluptatem.X") (split-by-char "aerat voluptatem."))))
  (is (= true (match-at-start (split-by-char "idunt ut labore et dolore magn") (split-by-char "idunt ut labore et d")))))

(deftest merge-fragments-test
  (is (= (split-by-char "m quaerat voluptatem.") (merge-fragments (split-by-char "m quaerat voluptatem.") (split-by-char "aerat voluptatem.") (split-by-char "aerat voluptatem."))))
  (is (= (split-by-char "abc def m quaerat voluptatem.") (merge-fragments (split-by-char "abc def m quaerat voluptatem.") (split-by-char "aerat voluptatem.") (split-by-char "aerat voluptatem."))))
  (is (= (split-by-char "pora incidunt ut labore et dolore magn") (merge-fragments (split-by-char "pora incidunt ut labore et d") (split-by-char "idunt ut labore et dolore magn") (split-by-char "idunt ut labore et d")))))


(deftest lorem-ipsum
  (is (= "Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem." (first (da-vynci fragments)))))
 

(clojure.test/run-tests)

