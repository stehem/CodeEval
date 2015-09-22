(ns bridges.baybridge-test
  (:require [clojure.test :refer :all]
            [bridges.core2 :refer :all]))

(def baybridges2
  {
   1 {:A {:lat 37.788353 :long -122.387695} :B {:lat 37.829853 :long -122.294312}}
   2 {:A {:lat 37.429615 :long -122.087631} :B {:lat 37.487391 :long -122.018967}}
   3 {:A {:lat 37.474858 :long -122.131577} :B {:lat 37.529332 :long -122.056046}}
   4 {:A {:lat 37.532599 :long -122.218094} :B {:lat 37.615863 :long -122.097244}}
   5 {:A {:lat 37.516262 :long -122.198181} :B {:lat 37.653383 :long -122.151489}}
   6 {:A {:lat 37.504824 :long -122.181702} :B {:lat 37.633266 :long -122.121964}} })



(deftest test-baybridge
  (let [cartesian-bridges (cartesian-bridges2 baybridges2)
        intersections (intersections cartesian-bridges)
        sorted-intersections (sort-by-number-of-intersects intersections)]
  (is (= '(1 2 3 5 6) (baybridge-challenge sorted-intersections)))))



(clojure.test/run-tests)
