(ns counterpoint.third-species
  (:require
   [counterpoint.first-species :refer [evaluate-species
                                       make-first-species]]
   [counterpoint.melody :refer [melody-score
                                quad-melody remove-last]]
   [counterpoint.species-type :refer [get-cantus get-counter
                                      get-low-high get-position make-species]]))

(defn make-third-species [cantus-firmus counterpoint-melody arg3]
  (make-species cantus-firmus counterpoint-melody arg3 :third))

(defn- get-low-high-third [species]
  (let [counter (get-counter species)
        quad-cantus (remove-last
                     (quad-melody (get-cantus species))
                     3)]
    (if (= (get-position species) :above)
      [quad-cantus counter]
      [counter quad-cantus])))

(defmethod get-low-high :third
  [species]
  (get-low-high-third species))

(defn evaluate-third-species [species & {:keys [verbose]
                                        :or {verbose false}}]
  (let [counter (get-counter species)
        ;; first-notes (conj
        ;;              (mapv first (partition 4 counter))
        ;;              (nth counter (dec (count counter))))
        ;; first-sp (make-first-species (get-cantus species)
        ;;                              first-notes
        ;;                              (get-position species))
        ;; first-score (evaluate-species first-sp)
        mel-score (melody-score counter :verbose verbose)]
    ;; (println "M-score" mel-score)
    ;; (println "1st-score" first-score)
    ;; (println (melody-skeleton counter))
    ;; (melody->lily (make-cantus-firmus :c (melody-skeleton counter)) {:file "resources/temp1.ly"})
    mel-score))

(defn third-species-rules? [species]
  ;; TODO
  true)
