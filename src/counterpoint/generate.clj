(ns counterpoint.generate

  (:require [counterpoint.cantus :refer [get-key get-melody]]
            [counterpoint.dfs.dfs :refer [generate-dfs-solutions]]
            [counterpoint.generate-first-species :refer [update-m36-size]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.utils :refer [dfs-solution->cp]]))

(defn solution? [{:keys [m36s]}]
  (and (= (get m36s :remaining-cantus-size) 0)
       (< (get m36s :no-suspensions-cnt 0) 2)))

(defn next-node [{:keys [position
                         key
                         melody
                         m36s ;; counter of thirds & sixths
                         previous-melody
                         previous-cantus
                         cantus-note
                         cantus-notes]}
                 current]
  {:position position
   :key key
   :melody (into melody current)
   :m36s (update-m36-size m36s position cantus-note current)
   :previous-melody (last current)
   :previous-cantus cantus-note
   :cantus-note (first cantus-notes)
   :cantus-notes (rest cantus-notes)})

(defn generate-reverse-counterpoint-dfs
  ([position key cantus candidates]
   (generate-reverse-counterpoint-dfs
    position key cantus candidates next-node))
  ([position key cantus candidates next-node]
   (let [rev-cantus (reverse cantus)
         m36s {:thirds 0 :sixths 0 :tens 0 :thirteens 0 :remaining-cantus-size (count rev-cantus)}
         melody []
         previous-melody nil
         previous-cantus nil
         root-node {:position position
                    :key key
                    :melody melody
                    :bar-melody []
                    :m36s m36s;; counter of thirds & sixths
                    :previous-melody previous-melody
                    :previous-cantus previous-cantus
                    :cantus-note (first rev-cantus)
                    :cantus-notes (rest rev-cantus)}]
     (generate-dfs-solutions root-node candidates next-node solution?))))

(defn generate-template
  ([candidates
    evaluate-species-fn
    make-species-fn
    species-rules-fn?]
   (generate-template candidates evaluate-species-fn
                      make-species-fn species-rules-fn?
                      next-node nil))
  ([candidates
    evaluate-species-fn
    make-species-fn
    species-rules-fn? next-node elaborate-fn]
   (fn [n cantus position options]
    ;; (println options)
     (let [key (get-key cantus)
           cf (get-melody cantus)
           cps (take n
                     (generate-reverse-counterpoint-dfs
                      position key cf candidates next-node))
           _ (println "ALL" (count cps))
          ;; _ (println "CPS" cps)
           species (if (> (count cps) 0)
                     (apply max-key #(let [e (evaluate-species-fn  %)]
                                ;; (println e)
                                       e)
                            (map #(make-species-fn cf (dfs-solution->cp %) position) cps))
                     nil)]
       (when (not (nil? species))
         (println "RULES " (species-rules-fn? species))
         (println "EVAL  " (evaluate-species-fn species :verbose true))

         (when (not (nil? elaborate-fn))
           (species->lily species {:file "temp-4"}))
         (let [elab-species (if (nil? elaborate-fn)
                              species
                              (elaborate-fn species key))]
           
         (spit (str "resources/" (get options :file "temp") ".clj")
               (str elab-species))

         (species->lily elab-species
                        {:clef (get options :clef)
                         :pattern (get options :pattern "")
                         :tempo (str "4 = " (get options :tempo 160))
                         :key key
                         :midi (get options :midi)
                         :file (get options :file "temp")})))
       species))))

