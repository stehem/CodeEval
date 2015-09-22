(ns bridges.core2
  (:use clojure.contrib.generic.functor)
  (:use bridges.cartesian)
  (:use bridges.intersect))


(defn toXY2
  [bridge]
  (let [A (get bridge :A) B (get bridge :B)
        Alat (get A :lat) Along (get A :long)
        Blat (get B :lat) Blong (get B :long)]
    {:A {:x (toX Alat Along) :y (toY Alat Along)}
     :B {:x (toX Blat Blong) :y (toY Blat Blong)}}))
  

(defn cartesian-bridges2
  [bridges]
  (fmap #(toXY2 %) bridges))


(defn bridge-intersect-bridge
  [bridge1 bridge2]
  (let [a1 (get bridge1 :A)
        b1 (get bridge1 :B)
        a2 (get bridge2 :A)
        b2 (get bridge2 :B)]
    (intersect a1 b1 a2 b2)))


(defn bridge-intersect-bridges
  [bridge bridges]
  (reduce
    (fn[memo x] 
      (if (and (not= (first x) (first bridge)) (bridge-intersect-bridge (second bridge) (second x)))
        (conj memo (first x)) memo ))
    []
    bridges ))


(defn intersections
  [bridges]
  (reduce 
    (fn[memo x] (assoc memo (first x) (bridge-intersect-bridges x bridges)))
    {}
    bridges))


(defn sort-by-number-of-intersects
  [intersections]
  (sort-by #(count (second %)) > intersections))


(defn remove-intersects
  [intersect intersects]
  (into [] (remove #(= intersect %) intersects)))


(defn map-removed-intersects
  [intersect intersects]
  (remove 
    #(= intersect (first %))
    (map 
      #(conj [] (first %) (remove-intersects intersect (second %))) 
      intersects)))


(defn has-intersect?
  [results]
  (some #(not (empty? (second %))) results ))


(defn clean-result
  [result]
  (sort < (map #(first %) result)))

(defn baybridge-challenge
  [intersects]
  (loop [result intersects]
    (if (not (has-intersect? result))
      (clean-result result)
      (let [biggest-interceptor (ffirst result)
            without-biggest-interceptor (map-removed-intersects biggest-interceptor result)]
        (recur without-biggest-interceptor)))))


