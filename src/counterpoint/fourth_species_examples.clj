(ns counterpoint.fourth-species-examples
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus :refer [get-melody]]
            [counterpoint.cantus-firmi-examples :refer [bellerman-a fux-d
                                                        haydn-d schenker-d
                                                        schoenberg-c]]
            [counterpoint.fourth-species :refer [evaluate-fourth-species
                                                 fourth-species-rules?
                                                 make-fourth-species]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.melody :refer [make-melody transpose]]
            [counterpoint.notes :as n]
            [counterpoint.rest :as rest]
            [counterpoint.species-type :refer [get-low-high]]))

(comment
  (def bethoven-haydn-d-4-species
    (make-fourth-species
     (get-melody haydn-d)
     (make-melody rest/r n/d4
                  n/d4 n/c4
                  n/c4 n/d4
                  n/d4 n/f4
                  n/f4 n/e4
                  n/e4 n/d4
                  n/d4 n/c4
                  n/c4 n/b4
                  n/a4 n/d4
                  n/d4 n/c#4
                  n/d4)
     :above))

  (evaluate-fourth-species bethoven-haydn-d-4-species :verbose true)
  (species->lily bethoven-haydn-d-4-species)

  (def fux-joseph-fux-d-4-species (make-fourth-species
                                   (get-melody fux-d)
                                   (make-melody rest/r n/a4
                                                n/a4 n/d4
                                                n/d4 n/c4
                                                n/c4 n/bb4
                                                n/bb4 n/g3
                                                n/a4 n/c4
                                                n/c4 n/f4
                                                n/f4 n/e4
                                                n/e4 n/d4
                                                n/d4 n/c#4
                                                n/d4)
                                   :above))
  (evaluate-fourth-species fux-joseph-fux-d-4-species :verbose true)
  (species->lily fux-joseph-fux-d-4-species)

  (def fux-aloys-fux-d-4-species (make-fourth-species
                                  (get-melody fux-d)
                                  (make-melody rest/r n/a4
                                               n/a4 n/d4
                                               n/d4 n/c4
                                               n/c4 n/bb4
                                               n/bb4 n/d4
                                               n/d4 n/c4 ;; hidden 5ths???
                                               n/c4 n/f4
                                               n/f4 n/e4
                                               n/e4 n/d4
                                               n/d4 n/c#4
                                               n/d4)
                                  :above))
  (evaluate-fourth-species fux-aloys-fux-d-4-species :verbose true)
  (species->lily fux-aloys-fux-d-4-species)

  (def salzer-fux-d-4-species (make-fourth-species
                               (get-melody fux-d)
                               (make-melody rest/r n/a4
                                            n/a4 n/d4
                                            n/d4 n/c4
                                            n/c4 n/b4
                                            n/b4 n/d4
                                            n/d4 n/a4
                                            n/a4 n/f4
                                            n/f4 n/e4
                                            n/e4 n/d4
                                            n/d4 n/c#4
                                            n/d4)
                               :above))
  (evaluate-fourth-species salzer-fux-d-4-species :verbose true)
  (species->lily salzer-fux-d-4-species)


  (def mozart-fux-d-4-species
    (make-fourth-species
     (get-melody fux-d)
     (make-melody rest/r n/a4
                  n/a4 n/d4
                  n/d4 n/c4
                  n/c4 n/b4
                  n/b4 n/e4
                  n/e4 n/d4
                  n/d4 n/c4
                  n/c4 n/b4
                  n/b4 n/a4
                  n/b4 n/c#4
                  n/d4)
     :above))
  (evaluate-fourth-species mozart-fux-d-4-species :verbose true)
  (species->lily mozart-fux-d-4-species)

  (def jeppesen-fux-d-4-species
    (make-fourth-species
     (get-melody fux-d)
     (make-melody rest/r n/a4
                  n/a4 n/d4
                  n/d4 n/c4
                  n/c4 n/b4
                  n/b4 n/e4
                  n/e4 n/d4
                  n/d4 n/c4
                  n/c4 n/bb4
                  n/a4 n/d4
                  n/d4 n/c#4
                  n/d4)
     :above))
  (evaluate-fourth-species jeppesen-fux-d-4-species :verbose true)
  (species->lily jeppesen-fux-d-4-species)

  (def bellermann-fux-d-4-species
    (make-fourth-species
     (get-melody fux-d)
     (make-melody rest/r n/d4
                  n/d4 n/c4
                  n/c4 n/b4
                  n/b4 n/d4
                  n/d4 n/e4
                  n/e4 n/d4
                  n/d4 n/c4
                  n/c4 n/b4
                  n/c4 n/d4
                  n/d4 n/c#4
                  n/d4)
     :above))
  (evaluate-fourth-species bellermann-fux-d-4-species :verbose true)
  (species->lily bellermann-fux-d-4-species)


  (def most-suspensions-fux-d (make-fourth-species
     (get-melody fux-d)
     (make-melody 
           :rest [:d 4 :natural]
            [:d 4 :natural] [:a 5 :natural]
            [:a 5 :natural] [:g 4 :natural]
            [:g 4 :natural] [:f 4 :natural]
            [:f 4 :natural] [:e 4 :natural]
            [:e 4 :natural] [:d 4 :natural]
            [:c 4 :natural] [:f 4 :natural]
            [:f 4 :natural] [:e 4 :natural]
            [:e 4 :natural] [:d 4 :natural]
            [:d 4 :natural] [:c 4 :sharp]
            [:d 4 :natural]) :above ))
  (evaluate-fourth-species most-suspensions-fux-d :verbose true)
  (species->lily most-suspensions-fux-d)


  (species->lily fux-d-4-species {:tempo "4=140" :midi "acoustic grand"})
  (fourth-species-rules? fux-d-4-species)
  (get-low-high fux-d-4-species)

  (fourth-species-rules? fux-d-4-species)
  (sh/sh "timidity" "resources/temp.midi")
;;   (sh/sh "timidity" "resources/temp.mid")

  (def bellerman-a-4-species (make-fourth-species
                              bellerman-a
                              (transpose (make-melody rest/r n/c4 n/c4
                                                      n/b4 n/b4 n/c4 n/d4 n/g4 n/g4
                                                      n/f4
                                                      n/f4
                                                      n/e4 n/e4
                                                      n/d4 n/a5 n/e4 n/f#4
                                                      n/g#4
                                                      n/a5) -1)
                              :above)))
(def bellerman-a-4-species-below
  (make-fourth-species
   bellerman-a
   (transpose
    (make-melody rest/r n/a4
                 n/c4 n/b4
                 n/b4 n/a4
                 n/a4 n/g3
                 n/g3 n/b4
                 n/b4 n/a4
                 n/a4 n/g3
                 n/e3 n/a4
                 n/a4 n/g#3
                 n/a4) -1)
   :below))

(species->lily
 bellerman-a-4-species-below
 {:pattern "abba" :tempo "4=120"
  :midi "acoustic grand"})

(comment
  (fourth-species-rules? bellerman-a-4-species-below)


  (def bellerman-a-4-species-below (make-fourth-species
                                    bellerman-a
                                    (transpose (make-melody
                                                rest/r n/a4 n/b4 n/g3
                                                n/a4
                                                n/c4
                                                n/c4 n/b4
                                                n/b4

                                                n/b5  n/b5 n/a5

                                            ;;  n/g3  n/a4 n/c4 

                                                n/d4 n/e4
                                                n/e4 n/a5
                                                n/a5 n/g#4
                                                n/a5) -1)
                                    :below))
  (species->lily bellerman-a-4-species-below {:pattern "" :clef "treble"})
  (fourth-species-rules? bellerman-a-4-species-below)
  (sh/sh "timidity" "resources/temp.midi")

  (def schenker-d-4-species (make-fourth-species
                             schenker-d
                             (make-melody rest/r n/d4
                                          n/d4 n/c4
                                          n/c4 n/a5

                                          n/a5 n/g4
                                          n/g4 n/f4
                                          n/f4 n/e4
                                          n/e4 n/b4

                                          n/c4 n/d4
                                          n/d4 n/c#4
                                          n/d4)
                             :above))

  (def schenker-d-4-species-2 (make-fourth-species
                               schenker-d
                               (make-melody rest/r n/d4
                                            n/d4 n/c4
                                            n/c4 n/a5

                                            n/a5 n/g4
                                            n/g4 n/f4
                                            n/f4 n/e4 ;; hidden octaves
                                            n/b4 n/e4

                                            n/e4 n/d4
                                            n/d4 n/c#4
                                            n/d4)
                               :above))

  (species->lily schenker-d-4-species-2 {:clef "treble"})
  (fourth-species-rules? schenker-d-4-species-2)
  (sh/sh "timidity" "resources/temp.midi")

  (def schoenberg-c-4-species (make-fourth-species
                               schoenberg-c
                               (make-melody rest/r n/g3
                                            n/g3 n/d3
                                            n/e3 n/d3
                                            n/d3 n/c3
                                            n/c3 n/e3
                                            n/e3 n/d3
                                            n/d3 n/c3
                                            n/c3 n/b3
                                            n/b3 n/d3
                                            n/d3 n/c3
                                            n/c3 n/b3
                                            n/c3)
                               :above))
  (species->lily schoenberg-c-4-species {:pattern "baraabra" :clef "treble_8" :tempo "4=120"})

  (fourth-species-rules? schoenberg-c-4-species)
  ;
  )


;; (sh/sh "timidity" "resources/temp.midi")


