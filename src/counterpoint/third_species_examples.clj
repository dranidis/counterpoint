(ns counterpoint.third-species-examples
  (:require [counterpoint.cantus :refer [get-melody]]
            [counterpoint.cantus-firmi-examples :refer [fux-d fux-e fux-f]]
            [counterpoint.first-species :refer [make-first-species]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.melody :refer [make-melody transpose]]
            [counterpoint.notes :as n]
            [counterpoint.rest :as r]
            [counterpoint.second-species :refer [make-second-species]]
            [counterpoint.third-species :refer [make-third-species]]
            [counterpoint.third-species-patterns :as p]))

;; 5 quarters asc or desc: 
;; 1st, 3rd and 5th consonant; 2nd and 4th are passing tones
;; 5 4 3 2 | 1
;; 5- 4- 3- 2- | 1
;; only exception 2nd (5b) and 3rd (4) may be dissonant


;; 1st 2nd 4th consonant; third may be dissonant as a passing tone

;; cambiata 
;; 2nd dissonant to consonant by skip (3rd)
;; 2 1 3- 2- | 1 (if 1 is dissonant 3- has to be consonant)

(defn- asc-pt-skip3-leap-to [k n]
  (p/asc-pt-skip3-leap-to {:key k :previous-melody n}))
(defn- asc-pt-skip3-to [k n]
  (p/asc-pt-skip3-to {:key k :previous-melody n}))
(defn- ending-below [k n]
  (p/ending-below {:key k :previous-melody n}))
(defn- ending-1 [k n]
  (p/asc-pt-to {:key k :previous-melody n}))
(defn- skip-4-asc-pt-to [k n]
  (p/skip-4-asc-pt-to {:key k :previous-melody n}))
(defn- skip-3-asc-pt-to [k n]
  (p/skip-3-asc-pt-to {:key k :previous-melody n}))
(defn- diss-3-desc-neighbor-to [k n]
  (p/diss-3-desc-neighbor-to {:key k :previous-melody n}))
(defn- diss-3-asc-neighbor-to [k n]
  (p/diss-3-asc-neighbor-to {:key k :previous-melody n}))
(defn- desc-pt-to [k n]
  (p/desc-pt-to {:key k :previous-melody n}))
(defn- desc-pt-skip-desc-3-to [k n]
  (p/desc-pt-skip-desc-3-to {:key k :previous-melody n}))
(defn- desc-pt-neighbor-to [k n]
  (p/desc-pt-neighbor-to {:key k :previous-melody n}))
(defn- cambiata-to [k n]
  (p/cambiata-to {:key k :previous-melody n}))
(defn- asc-pt-to [k n]
  (p/asc-pt-to {:key k :previous-melody n}))
(defn- skip-m6-asc-pt-to [k n]
  (p/skip-m6-asc-pt-to {:key k :previous-melody n}))

(defn- ending-1-e [k n]
  (p/ending-1-e {:key k :previous-melody n}))
(defn- desc-asc-pt-to [k n]
  (p/desc-asc-pt-to {:key k :previous-melody n}))
(defn- asc-desc-pt-to [k n]
  (p/asc-desc-pt-to {:key k :previous-melody n}))
(defn- ending-below-e [k n]
  (p/ending-below-e {:key k :previous-melody n}))
(defn- desc-pt-skip-3-to [k n]
  (p/desc-pt-skip-3-to {:key k :previous-melody n}))
(defn- chord-pt-to [k n]
  (p/chord-pt-to {:key k :previous-melody n}))







(comment
;; next-to-last
;; P4- P5- m3- m2- 
;; M2 P1 m3- m2-
  (species->lily
   (make-third-species
    (make-melody n/d3 n/e3 n/d3)
    (make-melody n/d4 n/c4 n/b4 n/a4 ;; passing tones 5 4 3 2 | 1
                 n/g3 n/a4 n/b4 n/c#4
                 n/d4) :above))


  (species->lily
   (make-third-species
    (make-melody n/a3 n/e3 n/d3)
    (make-melody n/c3 n/d3 n/e3 n/f3 ;; passing tones 5- 4- 3- 2- | 1
                 n/g3 n/a4 n/b4 n/c#4
                 n/d4) :above))


  (species->lily
   (make-third-species
    (make-melody n/b3 n/e3)
    (make-melody n/g4 n/f4 n/e4 n/d4 ;; exception
                 n/c4) :above))

  (species->lily
   (make-third-species
    (make-melody n/f3 n/e3 n/d3)
    (make-melody n/f4 n/e4 n/c4 n/d4 ;; cambiata
                 n/e4 n/d4 n/b4 n/c#4
                 n/d4) :above))

  (species->lily
   (make-third-species
    (make-melody n/a3 n/e3 n/d3)
    (make-melody n/f4 n/e4 n/c4 n/d4 ;; cambiata
                 n/e4 n/d4 n/b4 n/c#4
                 n/d4) :above))

  (species->lily
   (make-third-species
    (make-melody n/d3 n/e3 n/d3)
    (make-melody n/f3 n/a4 n/g3 n/f3 ;; 3rd diss
                 n/g3 n/a4 n/b4 n/c#4
                 n/d4) :above))

  (species->lily
   (make-third-species
    (make-melody n/d3 n/e3 n/d3)
    (make-melody n/f3 n/d3 n/e3 n/f3 ;; 3rd diss
                 n/g3 n/a4 n/b4 n/c#4
                 n/d4) :above))

  (species->lily
   (make-third-species
    (make-melody n/d3 n/e3 n/d3)
    (make-melody n/b4 n/a4 n/g3 n/f3 ;; 3rd diss
                 n/g3 n/a4 n/b4 n/c#4
                 n/d4) :above))

  (species->lily
   (make-third-species
    (make-melody n/a3 n/e3 n/d3)
    (make-melody n/f4 n/e4 n/d4 n/c4 ;; 3rd diss
                 n/g3 n/a4 n/b4 n/c#4
                 n/d4) :above))

  (species->lily
   (make-third-species
    (make-melody n/a3 n/e3 n/d3)
    (make-melody n/c4 n/a4 n/b4 n/c4 ;; 3rd diss
                 n/g3 n/a4 n/b4 n/c#4
                 n/d4) :above))

  (species->lily
   (make-third-species
    (make-melody n/f3 n/e3 n/d3)
    (make-melody r/r n/a4 n/b4 n/c4 ;; 3rd diss
                 n/g3 n/a4 n/b4 n/c#4
                 n/d4) :above))

;; FUX

  (species->lily
   (make-third-species
    (get-melody fux-d)
    (reduce
     (fn [l elements] (into l elements))
     []
     [(reverse (asc-pt-to :c n/a4)) ;; pt asc
      (reverse (asc-pt-to :c n/e4)) ;; pt asc
      (reverse (cambiata-to :c n/d4)) ;; cambiata
      (reverse (desc-pt-neighbor-to :c n/b4)) ;; diss 2)
      (reverse (asc-pt-to :c n/f4)) ;; pt asc

      (make-melody n/f4 n/f3 n/a4 n/b4) ;; ???

      (reverse (diss-3-asc-neighbor-to :c n/b4)) ;; diss 3 asc
      (diss-3-asc-neighbor-to :c n/a4) ;; shapes can also be used in reverse
      (reverse (skip-4-asc-pt-to :c n/g3)) ;; diss 3 ?
      (reverse (ending-1 :c n/d4))
      [n/d4]]) :above))

;; 5 quarters asc or desc: 
;; 1st, 3rd and 5th consonant; 2nd and 4th are passing tones
;; 5 4 3 2 | 1
;; 5- 4- 3- 2- | 1
;; only exception 2nd (5b) and 3rd (4) may be dissonant


;; 1st 2nd 4th consonant; third may be dissonant as a passing tone

;; cambiata 
;; 2nd dissonant to consonant by skip (3rd)
;; 2 1 3- 2- | 1 (if 1 is dissonant 3- has to be consonant)

  (species->lily
   (make-third-species
    (get-melody fux-d)
    (reduce
     (fn [l elements] (into l elements))
     []
     [(reverse (asc-pt-to :c n/a3))
      [n/a3 n/d2 n/a3 n/b3] ;; ????? three chord & PT
      (reverse (cambiata-to :c n/b3))
      (reverse (desc-pt-to :c n/e2))
      [n/e2 n/e3 n/b3 n/c3] ;; ????? three chord & PT
      [n/d3 n/a3 n/d2 n/e2] ;; ??? three chord & PT
      (reverse (asc-pt-to :c n/c3))
      (diss-3-desc-neighbor-to :c n/d3)
      [n/d3 n/a3 n/d2 n/d3] ;; ????? all chord tones
      (reverse (ending-below :c n/d3))
      [n/d3]]) :below)
   {:clef "treble_8"
    :pattern ""
    :tempo "2 = 80"
    :file "temp"
    :key :c})
  
  (comment
;; next-to-last
;; P4- P5- m3- m2- 
;; M2 P1 m3- m2-
    (species->lily
     (make-third-species
      (make-melody n/d3 n/e3 n/d3)
      (make-melody n/d4 n/c4 n/b4 n/a4 ;; passing tones 5 4 3 2 | 1
                   n/g3 n/a4 n/b4 n/c#4
                   n/d4) :above))


    (species->lily
     (make-third-species
      (make-melody n/a3 n/e3 n/d3)
      (make-melody n/c3 n/d3 n/e3 n/f3 ;; passing tones 5- 4- 3- 2- | 1
                   n/g3 n/a4 n/b4 n/c#4
                   n/d4) :above))


    (species->lily
     (make-third-species
      (make-melody n/b3 n/e3)
      (make-melody n/g4 n/f4 n/e4 n/d4 ;; exception
                   n/c4) :above))

    (species->lily
     (make-third-species
      (make-melody n/f3 n/e3 n/d3)
      (make-melody n/f4 n/e4 n/c4 n/d4 ;; cambiata
                   n/e4 n/d4 n/b4 n/c#4
                   n/d4) :above))

    (species->lily
     (make-third-species
      (make-melody n/a3 n/e3 n/d3)
      (make-melody n/f4 n/e4 n/c4 n/d4 ;; cambiata
                   n/e4 n/d4 n/b4 n/c#4
                   n/d4) :above))

    (species->lily
     (make-third-species
      (make-melody n/d3 n/e3 n/d3)
      (make-melody n/f3 n/a4 n/g3 n/f3 ;; 3rd diss
                   n/g3 n/a4 n/b4 n/c#4
                   n/d4) :above))

    (species->lily
     (make-third-species
      (make-melody n/d3 n/e3 n/d3)
      (make-melody n/f3 n/d3 n/e3 n/f3 ;; 3rd diss
                   n/g3 n/a4 n/b4 n/c#4
                   n/d4) :above))

    (species->lily
     (make-third-species
      (make-melody n/d3 n/e3 n/d3)
      (make-melody n/b4 n/a4 n/g3 n/f3 ;; 3rd diss
                   n/g3 n/a4 n/b4 n/c#4
                   n/d4) :above))

    (species->lily
     (make-third-species
      (make-melody n/a3 n/e3 n/d3)
      (make-melody n/f4 n/e4 n/d4 n/c4 ;; 3rd diss
                   n/g3 n/a4 n/b4 n/c#4
                   n/d4) :above))

    (species->lily
     (make-third-species
      (make-melody n/a3 n/e3 n/d3)
      (make-melody n/c4 n/a4 n/b4 n/c4 ;; 3rd diss
                   n/g3 n/a4 n/b4 n/c#4
                   n/d4) :above))

    (species->lily
     (make-third-species
      (make-melody n/f3 n/e3 n/d3)
      (make-melody r/r n/a4 n/b4 n/c4 ;; 3rd diss
                   n/g3 n/a4 n/b4 n/c#4
                   n/d4) :above))

;; FUX

    (species->lily
     (make-third-species
      (get-melody fux-d)
      (reduce
       (fn [l elements] (into l elements))
       []
       [(reverse (asc-pt-to :c n/a4)) ;; pt asc
        (reverse (asc-pt-to :c n/e4)) ;; pt asc
        (reverse (cambiata-to :c n/d4)) ;; cambiata
        (reverse (desc-pt-neighbor-to :c n/b4)) ;; diss 2)
        (reverse (asc-pt-to :c n/f4)) ;; pt asc

        (make-melody n/f4 n/f3 n/a4 n/b4) ;; chort-pt

        (reverse (diss-3-asc-neighbor-to :c n/b4)) ;; diss 3 asc
        (diss-3-asc-neighbor-to :c n/a4) ;; shapes can also be used in reverse
        (reverse (skip-4-asc-pt-to :c n/g3)) ;; diss 3 ?
        (reverse (ending-1 :c n/d4))
        [n/d4]]) :above))

    

;; e above
    (species->lily
     (make-third-species
      (get-melody fux-e)
      (reduce
       (fn [l elements] (into l elements))
       []
       [(reverse (skip-3-asc-pt-to :c n/c4))
        (reverse (desc-pt-to :c n/f3))
        (reverse (asc-pt-to :c n/c4))
        (reverse (skip-m6-asc-pt-to :c n/a4))
        [n/a4 n/c4 n/e4 n/d4] ;; ???? arpegio-pt
        (diss-3-asc-neighbor-to :c n/b4)
        [n/b4 n/d4 n/b4 n/a4] ;; ??? chord-pt
        [n/g3 n/b4 n/c4 n/b4] ;; ??? 
        (reverse (ending-1-e :c n/e4))
        [n/e4]]) :above)
     {:clef "treble"
      :pattern ""
      :tempo "2 = 80"
      :file "temp"
      :key :c})

;; e below
    (species->lily
     (make-third-species
      (get-melody fux-e)
      (reduce
       (fn [l elements] (into l elements))
       []
       [(reverse (asc-pt-skip3-leap-to :c n/a3))
        (reverse (desc-pt-to :c n/d2))
        (reverse (asc-pt-to :c n/a3))
        (reverse (chord-pt-to :c n/f2))
        (reverse (desc-asc-pt-to :c n/f2))
        (reverse (asc-pt-to :c n/c3))
        (reverse (asc-desc-pt-to :c n/c3))
        [n/c3 n/c2 n/c3 n/b3] ;; ??? chord-pt
        (reverse (ending-below-e :c n/e3))
        [n/e3]]) :below)
     {:clef "treble_8"
      :pattern ""
      :tempo "2 = 80"
      :file "temp"
      :key :c})


;; f above
    (species->lily
     (make-third-species
      (transpose (get-melody fux-f) -1)
      (reduce
       (fn [l elements] (into l elements))
       []
       [(reverse (desc-pt-to :c n/b3))
        [n/b3 n/d3 n/g3 n/f3] ;; chord-pt
        (reverse (desc-pt-to :f n/a3)) ;; in descending may take another key
        [n/a3 n/c3 n/d3 n/e3] ;; chord-pt
        (reverse (skip-3-asc-pt-to :c n/g3))
        (reverse (skip-3-asc-pt-to :c n/a4))
        (reverse (desc-pt-skip-3-to :c n/g3))
        (reverse (desc-pt-to :c n/c3))
        [n/c3 n/e3 n/c3 n/e3]
        (reverse (desc-pt-to :c n/bb3))
        (reverse (ending-1 :f n/f3))
        [n/f3]]) :above)
     {:clef "treble"
      :pattern ""
      :tempo "2 = 80"
      :file "temp"
      :key :c})

;; f below
    (species->lily
     (make-third-species
      (transpose (get-melody fux-f) -1)
      (reduce
       (fn [l elements] (into l elements))
       []
       [[n/f1 n/f2 n/e2 n/d2]
        (reverse (skip-3-asc-pt-to :c n/f2))
        (reverse (desc-pt-neighbor-to :c n/d2))
        (reverse (desc-pt-neighbor-to :c n/bb2))
        [n/bb2 n/f2 n/bb3 n/a3]
        [n/g2 n/c2 n/c3 n/bb3]
        [n/a3 n/g2 n/f2 n/d2]
        (reverse (skip-3-asc-pt-to :c n/f2))
        (reverse (desc-pt-neighbor-to :c n/d2))
        [n/d2 n/e2 n/f2 n/d2]
        (reverse (ending-below :c n/f2))
        [n/f2]]) :below)
     {:clef "bass"
      :pattern ""
      :tempo "2 = 80"
      :file "temp"
      :key :c})

    (species->lily
     (make-third-species
      (get-melody fux-d)
      (make-melody
       n/d3 n/e3 n/f3 n/g3 ;; pt asc
       n/a4 n/b4 n/c4 n/d4 ;; pt asc
       n/e4 n/d4 n/b4 n/c4 ;; cambiata
       n/d4 n/c4 n/bb4 n/a4 ;; diss 2
       n/bb4 n/c4 n/d4 n/e4 ;; pt asc
       n/f4 n/f3 n/a4 n/b4 ;; ???
       n/c4 n/a4 n/bb4 n/c4 ;; diss 3 asc
       n/bb4 n/a4 n/g3 n/bb4 ;; ??
       n/a4 n/d3 n/e3 n/f3 ;; diss 3 ?
       n/g3 n/a4 n/b4 n/c#4
       n/d4) :above))

    (species->lily
     (make-first-species
      (get-melody fux-d)
      (make-melody
       n/d3 ;; n/e3 n/f3 n/g3 ;; pt asc
       n/a4 ;; n/b4 n/c4 n/d4 ;; pt asc
       n/e4 ;; n/d4 n/b4 n/c4 ;; cambiata avoids parallel 8ths
       n/d4 ;; n/c4 n/bb4 n/a4 ;; diss 2
       n/bb4 ;; n/c4 n/d4 n/e4 ;; pt asc
       n/f4 ;; n/f3 n/a4 n/b4 ;; ???
       n/c4 ;; n/a4 n/bb4 n/c4 ;; diss 3 asc
       n/bb4 ;; n/a4 n/g3 n/bb4 ;; ??
       n/a4 ;; n/d3 n/e3 n/f3 ;; diss 3 ?
   ;;n/g3 ;; n/a4 n/b4 
       n/c#4
       n/d4) :above))

    (species->lily
     (make-second-species
      (get-melody fux-d)
      (make-melody
       n/d3 ;; n/e3 
       n/f3 ;; n/g3 ;; pt asc
       n/a4 ;; n/b4 
       n/c4 ;; n/d4 ;; pt asc
       n/e4 ;; n/d4 
       n/b4 ;; n/c4 ;; cambiata
       n/d4 n/c4 ;; next notes are anticipation of the next measure
     ;;n/bb4 ;; n/a4 ;; diss 2
       n/bb4 ;; n/c4 
       n/d4 ;; n/e4 ;; pt asc
       n/f4 ;; n/f3 
       n/a4 ;; n/b4 ;; ???
       n/c4 n/a4 ;; next notes are anticipation of the next measure
     ;; n/bb4 ;; n/c4 ;; diss 3 asc
       n/bb4 ;; n/a4 
       n/g3 ;; n/bb4 ;; ??
       n/a4 n/d3 ;; next notes are connecting this note with the next 
     ;; n/e3 ;; n/f3 ;; diss 3 ?
       n/g3 ;; n/a4 
     ;;n/b4 ;; 
       n/c#4
       n/d4) :above))
   ;
    )

  (species->lily
   (make-third-species
    (get-melody fux-d)
    (reduce
     (fn [l elements] (into l elements))
     []
     [(reverse (asc-pt-to :c n/a3))
      [n/a3 n/d2 n/a3 n/b3] ;; ?????
      (reverse (cambiata-to :c n/b3))
      (reverse (desc-pt-to :c n/e2))
      [n/e2 n/e3 n/b3 n/c3] ;; ?????
      [n/d3 n/a3 n/d2 n/e2] ;; ???
      (reverse (asc-pt-to :c n/c3))
      (diss-3-desc-neighbor-to :c n/d3)
      [n/d3 n/a3 n/d2 n/d3] ;; ?????
      (reverse (ending-below :c n/d3))
      [n/d3]]) :below)
   {:clef "treble_8"
    :pattern ""
    :tempo "2 = 80"
    :file "temp"
    :key :c})

;; e above
  (species->lily
   (make-third-species
    (get-melody fux-e)
    (reduce
     (fn [l elements] (into l elements))
     []
     [(reverse (skip-3-asc-pt-to :c n/c4))
      (reverse (desc-pt-to :c n/f3))
      (reverse (asc-pt-to :c n/c4))
      (reverse (skip-m6-asc-pt-to :c n/a4))
      [n/a4 n/c4 n/e4 n/d4] ;; ???? chord-pt
      (diss-3-asc-neighbor-to :c n/b4)
      [n/b4 n/d4 n/b4 n/a4] ;; chord-pt
      [n/g3 n/b4 n/c4 n/b4] ;; chord
      (reverse (ending-1-e :c n/e4))
      [n/e4]]) :above)
   {:clef "treble"
    :pattern ""
    :tempo "2 = 80"
    :file "temp"
    :key :c})

;; e below
  (species->lily
   (make-third-species
    (get-melody fux-e)
    (reduce
     (fn [l elements] (into l elements))
     []
     [(reverse (asc-pt-skip3-leap-to :c n/a3))
      (reverse (desc-pt-to :c n/d2))
      (reverse (asc-pt-to :c n/a3))
      [n/a3 n/e2 n/a3 n/g2] ;; chord-pt
      (reverse (desc-asc-pt-to :c n/f2))
      (reverse (asc-pt-to :c n/c3))
      (reverse (asc-desc-pt-to :c n/c3))
      [n/c3 n/c2 n/c3 n/b3] ;; chord-pt
      (reverse (ending-below-e :c n/e3))
      [n/e3]]) :below)
   {:clef "treble_8"
    :pattern ""
    :tempo "2 = 80"
    :file "temp"
    :key :c})


;; f above
  (species->lily
   (make-third-species
    (transpose (get-melody fux-f) -1)
    (reduce
     (fn [l elements] (into l elements))
     []
     [(reverse (desc-pt-to :c n/b3))
      [n/b3 n/d3 n/g3 n/f3] ;; chord-pt
      (reverse (desc-pt-to :f n/a3)) ;; in descending may take another key
      [n/a3 n/c3 n/d3 n/e3] ;; chord-pt
      (reverse (skip-3-asc-pt-to :c n/g3))
      (reverse (skip-3-asc-pt-to :c n/a4))
      (reverse (desc-pt-skip-3-to :c n/g3))
      (reverse (desc-pt-to :c n/c3))
      [n/c3 n/e3 n/c3 n/e3] ;; chord
      (reverse (desc-pt-to :c n/bb3))
      (reverse (ending-1 :f n/f3))
      [n/f3]]) :above)
   {:clef "treble"
    :pattern ""
    :tempo "2 = 80"
    :file "temp"
    :key :c})

;; f below
  (species->lily
   (make-third-species
    (transpose (get-melody fux-f) -1)
    (reduce
     (fn [l elements] (into l elements))
     []
     [[n/f1 n/f2 n/e2 n/d2]
      (reverse (skip-3-asc-pt-to :c n/f2))
      (reverse (desc-pt-neighbor-to :c n/d2))
      (reverse (desc-pt-neighbor-to :c n/bb2))
      [n/bb2 n/f2 n/bb3 n/a3] ;; chord-pt
      [n/g2 n/c2 n/c3 n/bb3] ;;chord-pt
      (reverse (desc-pt-skip-desc-3-to :c n/e2))
      (reverse (skip-3-asc-pt-to :c n/f2))
      (reverse (desc-pt-neighbor-to :c n/d2))
      (reverse (asc-pt-skip3-to :c n/e2))
      (reverse (ending-below :c n/f2))
      [n/f2]]) :below)
   {:clef "bass"
    :pattern ""
    :tempo "2 = 80"
    :file "temp"
    :key :c})

  (species->lily
   (make-third-species
    (get-melody fux-d)
    (make-melody
     n/d3 n/e3 n/f3 n/g3 ;; pt asc
     n/a4 n/b4 n/c4 n/d4 ;; pt asc
     n/e4 n/d4 n/b4 n/c4 ;; cambiata
     n/d4 n/c4 n/bb4 n/a4 ;; diss 2
     n/bb4 n/c4 n/d4 n/e4 ;; pt asc
     n/f4 n/f3 n/a4 n/b4 ;; ???
     n/c4 n/a4 n/bb4 n/c4 ;; diss 3 asc
     n/bb4 n/a4 n/g3 n/bb4 ;; ??
     n/a4 n/d3 n/e3 n/f3 ;; diss 3 ?
     n/g3 n/a4 n/b4 n/c#4
     n/d4) :above))

  (species->lily
   (make-first-species
    (get-melody fux-d)
    (make-melody
     n/d3 ;; n/e3 n/f3 n/g3 ;; pt asc
     n/a4 ;; n/b4 n/c4 n/d4 ;; pt asc
     n/e4 ;; n/d4 n/b4 n/c4 ;; cambiata avoids parallel 8ths
     n/d4 ;; n/c4 n/bb4 n/a4 ;; diss 2
     n/bb4 ;; n/c4 n/d4 n/e4 ;; pt asc
     n/f4 ;; n/f3 n/a4 n/b4 ;; ???
     n/c4 ;; n/a4 n/bb4 n/c4 ;; diss 3 asc
     n/bb4 ;; n/a4 n/g3 n/bb4 ;; ??
     n/a4 ;; n/d3 n/e3 n/f3 ;; diss 3 ?
   ;;n/g3 ;; n/a4 n/b4 
     n/c#4
     n/d4) :above))

  (species->lily
   (make-second-species
    (get-melody fux-d)
    (make-melody
     n/d3 ;; n/e3 
     n/f3 ;; n/g3 ;; pt asc
     n/a4 ;; n/b4 
     n/c4 ;; n/d4 ;; pt asc
     n/e4 ;; n/d4 
     n/b4 ;; n/c4 ;; cambiata
     n/d4 n/c4 ;; next notes are anticipation of the next measure
     ;;n/bb4 ;; n/a4 ;; diss 2
     n/bb4 ;; n/c4 
     n/d4 ;; n/e4 ;; pt asc
     n/f4 ;; n/f3 
     n/a4 ;; n/b4 ;; ???
     n/c4 n/a4 ;; next notes are anticipation of the next measure
     ;; n/bb4 ;; n/c4 ;; diss 3 asc
     n/bb4 ;; n/a4 
     n/g3 ;; n/bb4 ;; ??
     n/a4 n/d3 ;; next notes are connecting this note with the next 
     ;; n/e3 ;; n/f3 ;; diss 3 ?
     n/g3 ;; n/a4 
     ;;n/b4 ;; 
     n/c#4
     n/d4) :above))
   ;
  )