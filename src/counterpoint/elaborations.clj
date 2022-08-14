(ns counterpoint.elaborations)

(defn elaborate-4th [[{:keys [d n]}]]
  (when (not= 2 d)
    (throw (Exception. (str "elaborate-4th: wrong duration :d" d))))
  (let [[n1 n2] n]
    (println "N1 N2" n1 n2)
    [{:d 2 :n [n1]}
     {:d 4 :n [n1 n2]}
     ]))