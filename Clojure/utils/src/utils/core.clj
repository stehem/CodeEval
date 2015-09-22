(ns utils.core)

(defn has-item?
    [coll item]
    (some #(= item %) coll))

(defn remove-nils
    [coll]
    (remove #(nil? %) coll))

(defn has-duplicates?
    [coll]
    (not= (count coll) (count (distinct coll))))

