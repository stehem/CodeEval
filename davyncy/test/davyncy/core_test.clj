(ns davyncy.core-test
  (:require [clojure.test :refer :all]
            [davyncy.core :refer :all]))

(deftest overlap-test
  (is (= '("D" "E" "F")  (find-overlap "ABCDEF" "DEFG")))
  (is (= '("A" "B" "C")  (find-overlap "ABCDEF" "XYZABC")))
  (is (= '("B" "C" "D" "E")  (find-overlap "ABCDEF" "BCDE")))
  (is (= nil  (find-overlap "ABCDEF" "XCDEZ"))))

(clojure.test/run-tests)

