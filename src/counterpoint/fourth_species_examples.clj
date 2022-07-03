(ns counterpoint.fourth-species-examples 
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [bellerman-a fux-d
                                                        schenker-d schoenberg-c]]
            [counterpoint.figured-bass :refer [figured-bass-fourth]]
            [counterpoint.fourth-species :refer [fourth-species-rules?
                                                 make-fourth-species]]
            [counterpoint.lilypond :refer [fourth-species->lily]]
            [counterpoint.melody :refer [make-melody transpose]]
            [counterpoint.notes :as n]))

(def fux-d-4-species (make-fourth-species
                      fux-d
                      (make-melody n/a4 n/a4
                                   n/d4 n/d4 n/c4 n/c4 n/bb4 n/bb4
                                   n/g3
                                   n/a4
                                   n/c4 n/c4
                                   n/f4 n/f4 n/e4 n/e4 n/d4 n/d4 n/c#4
                                   n/d4)
                      :above))
(fourth-species->lily fux-d-4-species "treble")
(fourth-species-rules? fux-d-4-species)
(figured-bass-fourth fux-d-4-species)
;; (sh/sh "timidity" "resources/temp.midi")
(sh/sh "timidity" "resources/temp.mid")



(count bellerman-a)
(def bellerman-a-4-species (make-fourth-species
                            bellerman-a
                            (make-melody n/c4 n/c4
                                         n/b4 n/b4 n/c4 n/d4 n/g4 n/g4
                                         n/f4
                                         n/f4
                                         n/e4 n/e4
                                         n/d4 n/a5 n/e4 n/f#4
                                         n/g#4
                                         n/a5)
                            :above))
(fourth-species->lily bellerman-a-4-species "treble")
(fourth-species-rules? bellerman-a-4-species)


(def bellerman-a-4-species-below (make-fourth-species
                                  bellerman-a
                                  (transpose (make-melody
                                              n/a4 n/b4 n/g3
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
(fourth-species->lily bellerman-a-4-species-below "treble")
(fourth-species-rules? bellerman-a-4-species-below)
;; (sh/sh "timidity" "resources/temp.midi")

(def schenker-d-4-species (make-fourth-species
                           schenker-d
                           (make-melody n/d4
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
(fourth-species->lily schenker-d-4-species "treble")
(fourth-species-rules? schenker-d-4-species)
(sh/sh "timidity" "resources/temp.midi")

(def schoenberg-c-4-species (make-fourth-species
                             schoenberg-c
                             (make-melody n/g3
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
;; (fourth-species->lily schoenberg-c-4-species "treble_8")

(fourth-species-rules? schoenberg-c-4-species)


;; (sh/sh "timidity" "resources/temp.midi")


