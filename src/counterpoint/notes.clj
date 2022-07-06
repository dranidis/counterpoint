(ns counterpoint.notes)

(defn make-note-nooctave [n acc]
  [n acc])

(defn get-note-nooctave [[n _]] n)
(defn get-acc-nooctave [[_ a]] a)

(defn make-note
  ([n o] (make-note n o :natural))
  ([n o accidental]
   [n o accidental]))

(defn get-note   [[n _ _]] n)
(defn get-octave [[_ o _]] o)
(defn get-acc    [[_ _ acc]] acc)

(defn get-nooctave [[n _ acc]]
  (make-note-nooctave n acc))

(defn nooctave->note [[n acc] o]
  (make-note n o acc))



(defn note->num [note]
  (case note
    :a 0
    :b 1
    :c 2
    :d 3
    :e 4
    :f 5
    :g 6
    nil))

(defn num->note [num]
 (case (mod num 7)
   0 :a
   1 :b
   2 :c
   3 :d
   4 :e
   5 :f
   6 :g))

;; (defn num2->note [num]
;;   (let [num-mod (mod num 7)
;;         note (num->note num-mod) 
;;         octave (quot (if (pos? num)
;;                        (+ num num-mod)
;;                        (- num num-mod)) 7)]
;;     [note octave]))

(defn num2->note [num]
  (let [num-mod (mod num 7)
        note (num->note num-mod)
        octave (+ (quot num 7) (if (and 
                                    (neg? num)
                                    (not= num-mod 0)) -1 0))]
    [note octave]))


(comment
  
  (num2->note -7)
(map num2->note (range -15 1))

  ;
  )






(def d2 (make-note :d 2))
(def e2 (make-note :e 2))
(def f2 (make-note :f 2))
(def f3 (make-note :f 3))
(def f4 (make-note :f 4))

(def f#2 (make-note :f 2 :sharp))
(def f#3 (make-note :f 3 :sharp))
(def f#4 (make-note :f 4 :sharp))

(def g1 (make-note :g 1))
(def g2 (make-note :g 2))
(def g3 (make-note :g 3))
(def g4 (make-note :g 4))

(def g#1 (make-note :g 1 :sharp))
(def g#2 (make-note :g 2 :sharp))
(def g#3 (make-note :g 3 :sharp))
(def g#4 (make-note :g 4 :sharp))
(def g#5 (make-note :g 5 :sharp))

(def g5 (make-note :g 5))

(def a2 (make-note :a 2))
(def a3 (make-note :a 3))
(def a4 (make-note :a 4))
(def a5 (make-note :a 5))

(def bb3 (make-note :b 3 :flat))
(def bb4 (make-note :b 4 :flat))
(def bb5 (make-note :b 5 :flat))

(def b2 (make-note :b 2))
(def b3 (make-note :b 3))
(def b#3 (make-note :b 3 :sharp))

(def c2 (make-note :c 2))
(def c3 (make-note :c 3))
(def c4 (make-note :c 4))
(def c5 (make-note :c 5))

(def d3 (make-note :d 3))
(def d4 (make-note :d 4))
(def d5 (make-note :d 5))

(def d#3 (make-note :d 3 :sharp))
(def d#4 (make-note :d 4 :sharp))
(def d#5 (make-note :d 5 :sharp))

(def c#3 (make-note :c 3 :sharp))
(def e3 (make-note :e 3))
(def e4 (make-note :e 4))
(def e5 (make-note :e 5))
(def b4 (make-note :b 4))
(def b5 (make-note :b 5))
(def c#4 (make-note :c 4 :sharp))
