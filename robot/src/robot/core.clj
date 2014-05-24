(ns robot.core
  (:use utils.core))


(defn edge-generator
  [base-number to-add times]
  (take times (iterate #(+ % to-add)  base-number)))


(defn edges
  [height width]
  (let [up (edge-generator 1 1 width)
        left (edge-generator 1 width height)
        bottom (edge-generator (last left) 1 width)
        right (edge-generator (last up) width height)]
    {:up up :left left :bottom bottom :right right}))


(defn possible-moves
  [position edges]
  (let [width (count (get edges :up))]
  (remove-nils [
                (if (not (has-item? (get edges :up) position))
                  (- position width)) 
                (if (not (has-item? (get edges :right) position))
                  (+ position 1))
                (if (not (has-item? (get edges :left) position))
                  (- position 1))
                (if (not (has-item? (get edges :bottom) position))
                  (+ position width))])))


(defn add-possible-moves
  [path edges]
  (if (= (last (get edges :bottom)) (last path))
    [path]
    (remove #(has-duplicates? %)
      (map #(conj path %) (possible-moves (last path) edges)))))


(defn move
  [paths edges]
  (mapcat #(add-possible-moves % edges) paths))


(defn all-have-arrived?
  [paths finish]
  (every? #(= finish (last %)) paths))


(defn robot-paths
  [height width]
  (let [edges (edges 4 4)]
    (loop [paths [[1]]]
      (if (all-have-arrived? paths (last (get edges :bottom)))
        (count paths)
        (recur (move paths edges))))))




