(ns bridges.intersect)

(defn ua_t
  [a1 a2 b1 b2]
  ;var ua_t = (b2.x - b1.x) * (a1.y - b1.y) - (b2.y - b1.y) * (a1.x - b1.x);
  (let [b2x-b1x (- (:x b2) (:x b1))
        a1y-b1y (- (:y a1) (:y b1))
        b2y-b1y (- (:y b2) (:y b1))
        a1x-b1x (- (:x a1) (:x b1))] 
    (- (* b2x-b1x a1y-b1y) (* b2y-b1y a1x-b1x))))


(defn ub_t
  [a1 a2 b1 b2]
  ;var ub_t = (a2.x - a1.x) * (a1.y - b1.y) - (a2.y - a1.y) * (a1.x - b1.x);
  (let [a2x-a1x (- (:x a2) (:x a1))
        a1y-b1y (- (:y a1) (:y b1))
        a2y-a1y (- (:y a2) (:y a1))
        a1x-b1x (- (:x a1) (:x b1))] 
    (- (* a2x-a1x a1y-b1y) (* a2y-a1y a1x-b1x))))


(defn ub
  [a1 a2 b1 b2]
  ;var u_b  = (b2.y - b1.y) * (a2.x - a1.x) - (b2.x - b1.x) * (a2.y - a1.y)
  (let [b2y-b1y (- (:y b2) (:y b1))
        a2x-a1x (- (:x a2) (:x a1))
        b2x-b1x (- (:x b2) (:x b1))
        a2y-a1y (- (:y a2) (:y a1))] 
    (- (* b2y-b1y a2x-a1x) (* b2x-b1x a2y-a1y))))


(defn intersect
  [a1 a2 b1 b2]
  (let [ua_t (ua_t a1 a2 b1 b2)
        ub_t (ub_t a1 a2 b1 b2)
        u_b (ub a1 a2 b1 b2)]
    (if (not= 0 u_b)
      (let [ua (/ ua_t u_b) 
            ub (/ ub_t u_b)]
        (if (and (<= 0 ua) (<= ua 1) (<= 0 ub) (<= ub 1))
          true
          false))
      false)))







