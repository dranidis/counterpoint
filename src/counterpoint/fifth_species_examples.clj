(ns counterpoint.fifth-species-examples
  (:require [counterpoint.cantus :refer [get-melody]]
            [counterpoint.cantus-firmi-examples :refer [fux-d mozart-d]]
            [counterpoint.fifth-species :refer [make-fifth-species]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.notes :as n]
            [counterpoint.rest :as r]))

(def fux-5th (let [notes [[{:d 2  :n [r/r n/a4]}]
                          [{:d 4 :n [n/a4 n/d3 n/e3 n/f3]}]
                          [{:d 4 :n [n/g3 n/f3 n/e3 n/g3]}]
                          [{:d 4 :n [n/f3 n/d3]} {:d 2 :n [n/d4]}]
                          [{:d 4 :n [n/d4 n/c4 n/bb4 n/g3]}]
                          [{:d 4 :n [n/a4 n/b4]} {:d 2 :n [n/c4]}]
                          [{:d 2 :n  [n/c4 n/f4]}]
                          [{:d 4 :n [n/f4]} {:d 8 :n [n/e4 n/d4]} {:d 2 :n [n/e4]}]
                          [{:d 4 :n [n/e4 n/a4]} {:d 2 :n [n/d4]}]
                          [{:d 2 :n [n/d4 n/c#4]}]
                          [{:d 1 :n [n/d4]}]]
                   sp (make-fifth-species (get-melody fux-d) notes :above)]
               sp))

;; (species->lily fux-5th)

(def mozart-fux-5th
  (let [notes [[{:d 2  :n [r/r n/a4]}]
               [{:d 4 :n [n/a4 n/g3 n/f3 n/a4]}]
               [{:d 4 :n [n/g3 n/f3 n/e3 n/g3]}]
               [{:d 4 :n [n/f3 n/a4]} {:d 2 :n [n/d4]}]
               [{:d 4 :n [n/d4 n/c4]} {:d 2 :n [n/bb4]}]
               [{:d 4 :n [n/bb4]} {:d 8 :n [n/a4 n/g3]} {:d 4 :n [n/a4 n/b4]}]
               [{:d 4 :n [n/c4 n/f3]} {:d 2 :n [n/f4]}]
               [{:d 4 :n [n/f4]} {:d 8 :n [n/e4 n/d4]} {:d 2 :n [n/e4]}]
               [{:d 4 :n [n/e4 n/a4]} {:d 2 :n [n/d4]}]
               [{:d 4 :n [n/d4]} {:d 8 :n [n/c#4 n/b4]} {:d 2 :n [n/c#4]}]
               [{:d 1 :n [n/d4]}]]
        sp (make-fifth-species (get-melody fux-d) notes :above)]
    sp))

(species->lily mozart-fux-5th)

(def mozart-fux-5th-below
  (let [notes [[{:d 2  :n [r/r n/d2]}]
               [{:d 4 :n [n/d2 n/e2 n/f2 n/g2]}]
               [{:d 4 :n [n/a3 n/b3]} {:d 2 :n [n/c3]}]

               [{:d 4 :n [n/c3]}
               ;; suspension elaboration from above
                {:d 8 :n [n/d3 n/c3]}
                ;; resolution and then neighbor tone (beat 4 )
                {:d 4 :n [n/b3 n/a3]}]
               [{:d 4 :n [n/b3 n/g2]} {:d 2 :n [n/e3]}]
               [{:d 4 :n [n/e3]} {:d 8 :n [n/d3 n/c3]} {:d 4 :n [n/d3 n/e3]}]
               [{:d 4 :n [n/f3 n/e3 n/d3 n/c3]}]
              ;;  use of leading tone before the cadence
               [{:d 4 :n [n/b3 n/a3 n/b3 n/c#3]}]
               [{:d 4 :n [n/d3 n/a3]} {:d 2 :n [n/d3]}]
               [{:d 4 :n [n/d3]} {:d 8 :n [n/c#3 n/b3]} {:d 2 :n [n/c#3]}]
               [{:d 1 :n [n/d3]}]]
        sp (make-fifth-species (get-melody fux-d) notes :below)]
    sp))

(def mozart-mozart-d-5th-below
  (let [notes [[{:d 2  :n [r/r n/d3]}]
               [{:d 4 :n [n/d3]} {:d 8 :n [n/c3 n/b3]} {:d 4 :n [n/c3 n/bb3]}]
               [{:d 4 :n [n/a3 n/g2]} {:d 2 :n [n/f2]}]
               ;; neighbor tone on beat 2
               [{:d 4 :n [n/f2 n/e2 n/f2 n/d2]}]
               [{:d 4 :n [n/e2 n/g2]} {:d 2 :n [n/c3]}]
               [{:d 4 :n [n/c3 n/g2 n/c3 n/bb3]}]
               [{:d 4 :n [n/a3 n/g2]} {:d 2 :n [n/f2]}]
               ;; !! dissonant 4th a3
               [{:d 4 :n [n/f2 n/a3]} {:d 2 :n [n/d3]}]
               [{:d 4 :n [n/d3]} {:d 8 :n [n/c#3 n/b3]} {:d 2 :n [n/c#3]}]
               [{:d 1 :n [n/d3]}]]
        sp (make-fifth-species (get-melody mozart-d) notes :below)]
    sp))

(species->lily mozart-mozart-d-5th-below {:clef "treble_8"})

(def shenker-fux-5th
  (let [notes [[{:d 2  :n [r/r n/d4]}]
               [{:d 4 :n [n/d4 n/c4 n/a4 n/b4]}] ;; cambiata
               [{:d 4 :n [n/c4 n/g3]} {:d 2 :n [n/c4]}]
               [{:d 2 :n [n/c4]} {:d 4 :n [n/b4 n/a4]}]
               [{:d 4 :n [n/b4 n/d4 n/e4 n/b4]}]
               [{:d 4 :n [n/c4 n/b4 n/a4 n/b4]}]
               [{:d 4 :n [n/c4 n/f3]} {:d 2 :n [n/f4]}]
               [{:d 4 :n [n/f4]} {:d 8 :n [n/e4 n/d4]} {:d 2 :n [n/e4]}]
               [{:d 4 :n [n/e4 n/a4]} {:d 2 :n [n/d4]}]
               [{:d 2 :n [n/d4 n/c#4]}]
               [{:d 1 :n [n/d4]}]]
        sp (make-fifth-species (get-melody fux-d) notes :above)]
    sp))

;; (species->lily shenker-fux-5th)
