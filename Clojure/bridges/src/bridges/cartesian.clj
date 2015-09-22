(ns bridges.cartesian
  (:use clojure.contrib.generic.math-functions))

(def earth_radius
  6371)

(defn toX
  [lat long]
  (* earth_radius (* (cos lat) (cos long))))

(defn toY
  [lat long]
  (* earth_radius (* (cos lat) (sin long))))
