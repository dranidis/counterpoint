(ns counterpoint.notes)

(defn make-note
  ([n o] (make-note n o :natural))
  ([n o accidental]
   [n o accidental]))

(defn get-note
  [[n _ _]] n)
(defn get-octave
  [[_ o _]] o)
(defn get-acc
  [[_ _ acc]] acc)

(def f2 (make-note :f 2))
(def f3 (make-note :f 3))
(def f#3 (make-note :f 3 :sharp))
(def g2 (make-note :g 2))
(def g3 (make-note :g 3))
(def g#3 (make-note :g 3 :sharp))
(def g5 (make-note :g 5))
(def a3 (make-note :a 3))
(def bb3 (make-note :b 3 :flat))
(def b3 (make-note :b 3))
(def b#3 (make-note :b 3 :sharp))
(def c3 (make-note :c 3))
(def c#3 (make-note :c 3 :sharp))
(def e3 (make-note :e 3))
(def e4 (make-note :e 4))
(def e5 (make-note :e 5))
(def a4 (make-note :a 4))
(def b4 (make-note :b 4))
(def bb4 (make-note :b 4 :flat))
(def c#4 (make-note :c 4 :sharp))
