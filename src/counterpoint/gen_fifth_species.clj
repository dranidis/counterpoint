(ns counterpoint.gen-fifth-species 
  (:require [counterpoint.dfs.dfs :refer [generate-dfs-solutions]]
            [counterpoint.fifth-species :refer [evaluate-fifth-species
                                                fifth-species-rules?
                                                make-fifth-species]]
            [counterpoint.gen-first-dfs :refer [solution?]]
            [counterpoint.generate :refer [generate-template]]))

(defn generate-reverse-counterpoint-5th-dfs [position key cantus]
  (let [rev-cantus (reverse cantus)
        m36s {:thirds 0 :sixths 0 :tens 0 :thirteens 0 :remaining-cantus-size (count rev-cantus)}
        melody []
        previous-melody nil
        previous-cantus nil
        root-node {:position position
                   :key key
                   :melody melody
                   :m36s m36s;; counter of thirds & sixths
                   :previous-melody previous-melody
                   :previous-cantus previous-cantus
                   :cantus-note (first rev-cantus)
                   :cantus-notes (rest rev-cantus)}]
    (generate-dfs-solutions root-node candidates next-node solution?)))

(def generate-fifth
  (generate-template
   generate-reverse-counterpoint-5th-dfs
   evaluate-fifth-species
   make-fifth-species
   fifth-species-rules?))