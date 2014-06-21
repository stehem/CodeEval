(ns davyncy.core)

(def text
  "m quaerat voluptatem.;pora incidunt ut labore et d;, consectetur, adipisci velit;olore magnam aliqua;idunt ut labore et dolore magn;uptatem.;i dolorem ipsum qu;iquam quaerat vol;psum quia dolor sit amet, consectetur, a;ia dolor sit amet, conse;squam est, qui do;Neque porro quisquam est, qu;aerat voluptatem.;m eius modi tem;Neque porro qui;, sed quia non numquam ei;lorem ipsum quia dolor sit amet;ctetur, adipisci velit, sed quia non numq;unt ut labore et dolore magnam aliquam qu;dipisci velit, sed quia non numqua;us modi tempora incid;Neque porro quisquam est, qui dolorem i;uam eius modi tem;pora inc;am al")

(defn to-fragments
  [raw-text]
  (clojure.string/split raw-text #";"))

(def fragments
  (to-fragments text))

(defn split-by-char
  [string]
  (rest (clojure.string/split string #"")))

(defn pad-front
  [times coll]
  (if (= 0 times)
    coll
    (last (take times (iterate #(conj % nil) (into coll [nil]))))))

(defn pad-back
  [times coll]
  (if (= 0 times)
    coll
    (reverse (pad-front times (reverse coll)))))

(defn pad
  [times-front times-back coll]
  (pad-back times-back (pad-front times-front coll)))

(defn padded-without-nils
  [coll]
  (remove #(= nil (second %))
    (map-indexed vector coll)))

(defn bounds
  [coll]
  (let [padded-without-nils (padded-without-nils coll)]
  [(first (first padded-without-nils)) (first (last padded-without-nils))]))

(defn interval-overlaps
  [int1 int2]
  (let [all (sort (into int1 int2))]
    [(nth all 1) (nth all 2)]))

(defn sub-seq
  [itvl coll]
  (let [takee (+ 1 (- (second itvl) (first itvl)))
        dropee (first itvl)]
    (take takee (drop dropee coll))))

(defn slide
  [total start-pad reference-pad]
  (loop [start 0
         end total
         overlap nil]
      (if (= -1 end)
        overlap
        (let [new-pad (pad start end start-pad)
              new-pad-bounds (bounds new-pad)
              itvl-overlaps (interval-overlaps (bounds reference-pad) new-pad-bounds)
              sub-seq-new-pad (sub-seq itvl-overlaps new-pad)
              sub-seq-reference (sub-seq itvl-overlaps reference-pad)
              overlap? (= sub-seq-new-pad sub-seq-reference)]
          (recur (inc start) (dec end)
            (if overlap? 
              (if (or (nil? overlap) (> (count (sub-seq-new-pad)) (count overlap)))
                sub-seq-new-pad
                overlap)
              overlap))))))

(defn find-overlap
  [string1 string2]
  (let [total (- (+ (count string1) (count string2)) 2)
        smaller (if (>= (count string1) (count string2)) string2 string1)
        bigger (if (= smaller string1) string2 string1)
        start-pad (pad 0 total (split-by-char smaller))
        reference-pad (pad (- (count smaller) 1) (- (count smaller) 1) (split-by-char bigger))]
    (slide total start-pad reference-pad)))
