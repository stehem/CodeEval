(ns robot.core-test
  (:require [clojure.test :refer :all]
            [robot.core :refer :all]))

(deftest robots
  (testing "Number of paths"
    (is (= 184 (robot-paths 4 4)))))
