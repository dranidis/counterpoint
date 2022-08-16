(ns counterpoint.utils)

(defn remove-last
  ([list]
   (remove-last list 1))
  ([list num-elements]
  ;;  (println "REMOVE LAST" list num-elements)
   (let [list-vec (apply vector list)]
     (subvec list-vec 0 (- (count list-vec) num-elements)))))

(defn rule-warning [rule message-fun]
  (when (not rule)
    (prn (message-fun)))
  rule)

(defn reverse-bar-melody [bar-melody]
  (reverse
   (mapv
    (fn [b] (map #(update % :n reverse) (reverse b)))
    bar-melody)))

(defn get-4th-species-from-bar-melody [bar-melody]
   (apply concat (map (fn [[bar]] (:n bar)) bar-melody)))

(defn dfs-solution->cp [solution]
  (if (seq (:bar-melody solution)) ;; in the case of 5th species
    (into [] (reverse-bar-melody (:bar-melody solution)))
    (into [] (reverse (:melody solution)))))

