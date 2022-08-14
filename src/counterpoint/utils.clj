(ns counterpoint.utils)

(defn rule-warning [rule message-fun]
  (when (not rule)
    (prn (message-fun)))
  rule)

(defn dfs-solution->cp [solution]
  (if (seq (:bar-melody solution))
    (into [] (reverse (mapv (fn [[b]] [(update b :n reverse)])(:bar-melody solution))))
    (into [] (reverse (:melody solution)))))

