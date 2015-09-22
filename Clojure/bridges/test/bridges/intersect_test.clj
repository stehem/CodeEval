(ns bridges.intersect-test
  (:require [clojure.test :refer :all]
            [bridges.core2 :refer :all]
            [bridges.intersect :refer :all]))

(deftest test-intersect
  (testing "detects basic intersection 1"
  (let [result (intersect {:x 2 :y 0} {:x 2 :y 3} {:x 1 :y 1} {:x 3 :y 1})]
    (is (= true result)))))

(deftest test-intersect2
  (testing "detects basic intersection 2"
  (let [result (intersect {:x 2 :y 0} {:x 2 :y 3} {:x 1 :y 1} {:x 1 :y 1})]
    (is (= false result)))))

(deftest test-intersect-remover1
  (is (= '(1 2 3 5 6) (baybridge-challenge [[4 [5 6]] [6 [4]] [5 [4]] [3 []] [2 []] [1 []]]))))


(deftest test-intersect-remover2
  (is (= '(1 2 3 5 6) (baybridge-challenge [[4 [5 6 3]] [6 [4]] [5 [4]] [3 [4]] [2 []] [1 []]]))))


(deftest test-intersect-remover3
  (is (= '(1 2 3 5) (baybridge-challenge [[4 [5 6]] [6 [4 2]] [5 [4]] [2 [6]] [3 []] [1 []]]))))


(deftest test-intersect-remover4
  (is (= '(1 2 3 5) (baybridge-challenge [[4 [3 5 6]] [6 [4 2]] [5 [4]] [3 [4]] [2 [6]] [1 []]]))))


(clojure.test/run-tests)
