(ns counterpoint.cantus-firmi-examples
  (:require [counterpoint.cantus :refer [get-key make-cantus-firmus]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]))

(def fux-d (make-cantus-firmus :f
                               (make-melody n/d3 n/f3 n/e3 n/d3 n/g3 n/f3 n/a4 n/g3 n/f3 n/e3 n/d3)))
(def fux-e (make-cantus-firmus :c (make-melody n/e3 n/c3 n/d3 n/c3 n/a3 n/a4 n/g3 n/e3 n/f3 n/e3)))
(def fux-f (make-cantus-firmus :c (make-melody n/f3 n/g3 n/a4 n/f3 n/d3 n/e3 n/f3 n/c4 n/a4 n/f3 n/g3 n/f3)))
(def fux-g (make-cantus-firmus :c (make-melody n/g2 n/c3 n/b3 n/g2 n/c3 n/e3 n/d3 n/g3 n/e3 n/c3 n/d3 n/b3 n/a3 n/g2)))
(def fux-a (make-cantus-firmus :c (make-melody n/a3 n/c3 n/b3 n/d3 n/c3 n/e3 n/f3 n/e3 n/d3 n/c3 n/b3 n/a3)))
(def fux-c (make-cantus-firmus :c (make-melody n/c3 n/e3 n/f3 n/g3 n/e3 n/a4 n/g3 n/e3 n/f3 n/e3 n/d3 n/c3)))

(def haydn-d (make-cantus-firmus :c (make-melody n/d3 n/e3 n/f3 n/d3 n/a4 n/f3 n/e3 n/g3 n/f3 n/e3 n/d3)))
(def haydn-a (make-cantus-firmus :c (make-melody n/a3 n/c3 n/b3 n/e3 n/c3 n/f3 n/d3 n/e3 n/c3 n/b3 n/a3)))

(def salieri-c (make-cantus-firmus :c (make-melody n/c3 n/f3 n/e3 n/a4 n/g3 n/f3 n/e3 n/d3 n/c3)))
(def salieri-d (make-cantus-firmus :f (make-melody n/d3 n/e3 n/f3 n/d3 n/a4 n/e3 n/f3 n/d3 n/c#3 n/d3)))

(def mozart-c1 (make-cantus-firmus :c (make-melody n/c3 n/g3 n/e3 n/a4 n/g3 n/c3 n/f3 n/d3 n/e3 n/c3 n/d3 n/c3)))
(def mozart-c2 (make-cantus-firmus :c (make-melody n/c3 n/f3 n/e3 n/d3 n/g3 n/f3 n/e3 n/a4 n/d3 n/g3 n/c3 n/e3 n/f3 n/e3 n/d3 n/c3)))
(def mozart-d (make-cantus-firmus :c (make-melody n/d3 n/e3 n/f3 n/d3 n/g3 n/e3 n/f3 n/d3 n/e3 n/d3)))
(def mozart-d2 (make-cantus-firmus :c (make-melody n/d3 n/c3 n/d3 n/f3 n/e3 n/c3 n/e3 n/a4 n/d3 n/g3 n/f3 n/e3 n/d3)))



(def fetis-c (make-cantus-firmus :c (make-melody n/c3 n/d3 n/e3 n/c3
                                                 n/g3 n/f3 n/e3 n/a4
                                                 n/g3 n/c3 n/d3 n/f3
                                                 n/e3 n/d3 n/c3)))

(def cf-c (make-cantus-firmus :c (make-melody n/c3 n/d3 n/f3
                                              n/e3 n/c3 n/g3
                                              n/f3 n/e3 n/d3
                                              n/c3)))

(def cf-a (make-cantus-firmus :c (make-melody n/a3 n/b3 n/c3
                                              n/a3 n/f3 n/e3
                                              n/d3 n/e3 n/c3
                                              n/b3 n/a3)))

(def albrechtsberger-f (make-cantus-firmus :f (make-melody n/f3 n/g3 n/c3
                                                           n/a4 n/f3 n/d3
                                                           n/bb4 n/a4 n/g3 n/f3))) ;; should be in the key of f

; no 2nd below
(def albrechtsberger-d (make-cantus-firmus :f (make-melody n/d3 n/a4
                                                           n/e3 n/f3
                                                           n/d3 n/bb4
                                                           n/g3 n/a4
                                                           n/f3 n/e3 n/d3))) ;; should be in the key of f

; no 2nd below
(def boulanger-g (make-cantus-firmus :g (make-melody n/g3 n/f#3 n/g3
                                                     n/e3 n/d3 n/b3 n/c3 n/d3
                                                     n/b3 n/a3 n/g2))) ;; key of g

(def boulanger-e (make-cantus-firmus :g (make-melody n/e3 n/c4 n/g3
                                                     n/a4 n/f#3 n/d3 n/e3
                                                     n/g3 n/f#3 n/e3))) ;; key of g

(def bellerman-a (make-cantus-firmus :c (make-melody n/a4 n/g3 n/e3
                                                     n/b4 n/d4 n/c4 n/b4
                                                     n/c4 n/b4 n/a4)))

(def schenker-d (make-cantus-firmus :f (make-melody n/d3 n/e3 n/f3 n/g3 n/a4
                                                    n/e3 n/g3 n/f3 n/e3 n/d3)))

; no 2nd above or below
(def schoenberg-c (make-cantus-firmus :c (make-melody n/c3 n/b3 n/g2 n/a3
                                                      n/e2 n/f2 n/a3 n/d2
                                                      n/g2 n/f2 n/d2 n/c2)))

(def jeppesen-c
  (make-cantus-firmus
   :c
   (make-melody n/c3 n/e3 n/f3 n/g3
                n/e3 n/a4 n/g3 n/e3
                n/f3 n/e3 n/d3 n/c3)))

(def jeppesen-d
  (make-cantus-firmus
   :c
   (make-melody n/d3 n/a4 n/g3 n/f3
                n/e3 n/d3 n/f3 n/e3
                n/d3)))

(def test-cf (make-cantus-firmus :c (make-melody n/d3 n/f3 n/e3 n/d3)))
(def test-cf2 (make-cantus-firmus :c (make-melody n/d3 n/c3 n/bb3 n/c#3 n/d3)))
(def test-cf3 (make-cantus-firmus :c (make-melody n/d3 n/e3 n/f3 n/e3 n/c#3 n/d3)))

;; (melody->lily test-cf3 {:clef "treble_8"})


(def cf-catalog
  {:fux-d fux-d
   :fux-g fux-g
   :fux-a fux-a
   :fux-c fux-c
   :fux-e fux-e
   :fux-f fux-f
   :schoenberg-c schoenberg-c
   :salieri-c salieri-c
   :salieri-d salieri-d
   :schenker-d schenker-d
   :mozart-c1 mozart-c1
   :mozart-c2 mozart-c2
   :mozart-d mozart-d
   :mozart-d2 mozart-d2
   :albrechtsberger-d albrechtsberger-d
   :albrechtsberger-f albrechtsberger-f
   :haydn-a haydn-a
   :haydn-d haydn-d
   :boulanger-e boulanger-e
   :boulanger-g boulanger-g
   :bellerman-a bellerman-a
   :fetis-c fetis-c
   :jeppesen-c jeppesen-c
   :jeppesen-d jeppesen-d})