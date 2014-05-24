(ns davyncy.core-test
  (:require [clojure.test :refer :all]
            [davyncy.core :refer :all]))

(deftest has-character-test
  (is (= true) (has-character? "B" "ABCD" ))
  (is (= false) (has-character? "X" "ABCD" ))
  (is (= true) (has-character? "A" "ABCDA" )) )

(deftest has-substring-test
  (is (= true (has-substring? "BCD" "ABCDE")))
  (is (= false (has-substring? "BXD" "ABCDE")))
  (is (= false (has-substring? "ACD" "ABCDE")))
  (is (= false (has-substring? "BCY" "ABCDE")))
  (is (= true (has-substring? "BCD" "ABCDEBVG")))
  (is (= true (has-substring? "BCD" "ABCDEBCD"))))

(clojure.test/run-tests)
