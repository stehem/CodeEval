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

;ABCDE CDEFG

(defn has-character?
  [character string]
  (map #(first %)
    (filter #(= character (second %))
      (map-indexed vector (split-by-char string)))))


(defn has-substring?
  [substring string]
  (let [substring-split (split-by-char substring)
        string-split (split-by-char string)
        substring-start (first substring-split)
        substring-start-indices (has-character? substring-start string)
        substring-length (count substring-split)]
    (reduce
      (fn[memo x]  
        (let [possible-match (take substring-length (drop x string-split))]
          (if (or (= substring-split possible-match) (= true memo))
            true
            false)))
      false
      substring-start-indices)))
  





