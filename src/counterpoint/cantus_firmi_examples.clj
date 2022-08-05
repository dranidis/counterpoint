(ns counterpoint.cantus-firmi-examples
  (:require [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]))

(def fux-d (make-melody n/d3 n/f3 n/e3 n/d3 n/g3 n/f3 n/a4 n/g3 n/f3 n/e3 n/d3))
(def fux-e (make-melody n/e3 n/c3 n/d3 n/c3 n/a3 n/a4 n/g3 n/e3 n/f3 n/e3))
(def fux-f (make-melody n/f3 n/g3 n/a4 n/f3 n/d3 n/e3 n/f3 n/c4 n/a4 n/f3 n/g3 n/f3))
(def fux-g (make-melody n/g2 n/c3 n/b3 n/g2 n/c3 n/e3 n/d3 n/g3 n/e3 n/c3 n/d3 n/b3 n/a3 n/g2))
(def fux-a (make-melody n/a3 n/c3 n/b3 n/d3 n/c3 n/e3 n/f3 n/e3 n/d3 n/c3 n/b3 n/a3))
(def fux-c (make-melody n/c3 n/e3 n/f3 n/g3 n/e3 n/a4 n/g3 n/e3 n/f3 n/e3 n/d3 n/c3))

(def haydn (make-melody n/d3 n/e3 n/f3 n/d3 n/a4 n/f3 n/e3 n/g3 n/f3 n/e3 n/d3))
(def haydn-a (make-melody n/a3 n/c3 n/b3 n/e3 n/c3 n/f3 n/d3 n/e3 n/c3 n/b3 n/a3))
(def salieri-c (make-melody n/c3 n/f3 n/e3 n/a4 n/g3 n/f3 n/e3 n/d3 n/c3))
(def salieri-d (make-melody n/d3 n/e3 n/f3 n/d3 n/a4 n/e3 n/f3 n/d3 n/c#3 n/d3))

(def mozart-c1 (make-melody n/c3 n/g3 n/e3 n/a4 n/g3 n/c3 n/f3 n/d3 n/e3 n/c3 n/d3 n/c3))
(def mozart-c2 (make-melody n/c3 n/f3 n/e3 n/d3 n/g3 n/f3 n/e3 n/a4 n/d3 n/g3 n/c3 n/e3 n/f3 n/e3 n/d3 n/c3))


(def fetis-c (make-melody n/c3 n/d3 n/e3 n/c3
                          n/g3 n/f3 n/e3 n/a4
                          n/g3 n/c3 n/d3 n/f3
                          n/e3 n/d3 n/c3))

(def cf-c (make-melody n/c3 n/d3 n/f3
                       n/e3 n/c3 n/g3
                       n/f3 n/e3 n/d3
                       n/c3))

(def cf-a (make-melody n/a3 n/b3 n/c3
                       n/a3 n/f3 n/e3
                       n/d3 n/e3 n/c3
                       n/b3 n/a3))

(def albrechtsberger-f (make-melody n/f3 n/g3 n/c3
                                    n/a4 n/f3 n/d3
                                    n/bb4 n/a4 n/g3 n/f3)) ;; should be in the key of f

; no 2nd below
(def albrechtsberger-d (make-melody n/d3 n/a4
                                    n/e3 n/f3
                                    n/d3 n/bb4
                                    n/g3 n/a4
                                    n/f3 n/e3 n/d3)) ;; should be in the key of f

; no 2nd below
(def boulanger-g (make-melody n/g3 n/f#3 n/g3
                              n/e3 n/d3 n/b3 n/c3 n/d3
                              n/b3 n/a3 n/g2)) ;; key of g

(def boulanger-e (make-melody n/e3 n/c4 n/g3
                              n/a4 n/f#3 n/d3 n/e3
                              n/g3 n/f#3 n/e3)) ;; key of g

(def bellerman-a (make-melody n/a4 n/g3 n/e3
                              n/b4 n/d4 n/c4 n/b4
                              n/c4 n/b4 n/a4))

(def schenker-d (make-melody n/d3 n/e3 n/f3 n/g3 n/a4
                             n/e3 n/g3 n/f3 n/e3 n/d3))

; no 2nd above or below
(def schoenberg-c (make-melody n/c3 n/b3 n/g2 n/a3
                               n/e2 n/f2 n/a3 n/d2
                               n/g2 n/f2 n/d2 n/c2))

(def test-cf (make-melody n/d3 n/f3 n/e3 n/d3))
(def test-cf2 (make-melody n/d3 n/c3 n/bb3 n/c#3 n/d3))
(def test-cf3 (make-melody n/d3 n/e3 n/f3 n/e3 n/c#3 n/d3))

;; (melody->lily test-cf3 {:clef "treble_8"})


