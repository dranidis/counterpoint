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
(def salieri (make-melody n/c3 n/f3 n/e3 n/a4 n/g3 n/f3 n/e3 n/d3 n/c3))

(def mozart-c1 (make-melody n/c3 n/g3 n/e3 n/a4 n/g3 n/c3 n/f3 n/d3 n/e3 n/c3 n/d3 n/c3))


