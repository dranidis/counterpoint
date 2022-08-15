(ns counterpoint.fifth-species-examples
  (:require [counterpoint.cantus :refer [get-melody]]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
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
               [{:d 4 :n [n/bb4]} {:d 8 :n [n/a4 n/g3]} {:d 4 :n [n/a4 n/bb4]}]
               [{:d 4 :n [n/c4 n/f4]} {:d 2 :n [n/f4]}]
               [{:d 4 :n [n/f4]} {:d 8 :n [n/e4 n/d4]} {:d 2 :n [n/e4]}]
               [{:d 4 :n [n/e4 n/a4]} {:d 2 :n [n/d4]}]
               [{:d 4 :n [n/d4]} {:d 8 :n [n/c#4 n/b4]} {:d 2 :n [n/c#4]}]
               [{:d 1 :n [n/d4]}]]
        sp (make-fifth-species (get-melody fux-d) notes :above)]
    sp))

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

(species->lily shenker-fux-5th)
