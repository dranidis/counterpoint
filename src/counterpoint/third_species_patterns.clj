(ns counterpoint.third-species-patterns
  (:require [counterpoint.cantus :refer [maximum-range-M10?]]
            [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.generate-first-species :refer [crossing-filter
                                                         dim-or-aug-filter
                                                         max-harmonic-interval]]
            [counterpoint.intervals :refer [A4 d5- get-interval
                                            harmonic-consonant? m2- M2- m6- next-diatonic
                                            note-at-melodic-interval P4- P5 P8 P8- prev-diatonic]]
            [counterpoint.melody :refer [append-to-melody]]
            [counterpoint.motion :refer [direct-perfect?]]))

(defn asc-pt-to [{:keys [key previous-melody]}]
  (let [pt4 (prev-diatonic key previous-melody)
        pt3 (prev-diatonic key pt4)
        pt2 (prev-diatonic key pt3)
        pt1 (prev-diatonic key pt2)]
    [pt4 pt3 pt2 pt1]))

(defn desc-pt-to [{:keys [key previous-melody]}]
  (let [pt4 (next-diatonic key previous-melody)
        pt3 (next-diatonic key pt4)
        pt2 (next-diatonic key pt3)
        pt1 (next-diatonic key pt2)]
    [pt4 pt3 pt2 pt1]))

;;  c b a b | c
(defn desc-asc-pt-to [{:keys [key previous-melody]}]
  (let [pt4 (prev-diatonic key previous-melody)
        pt3 (prev-diatonic key pt4)
        pt2 pt4
        pt1 (next-diatonic key pt2) ;; previous-melody
        ]
    [pt4 pt3 pt2 pt1]))

;; c d e d | c
(defn asc-desc-pt-to [{:keys [key previous-melody]}]
  (let [pt4 (next-diatonic key previous-melody)
        pt3 (next-diatonic key pt4)
        pt2 pt4
        pt1 (prev-diatonic key pt2) ;; previous-melody
        ]
    [pt4 pt3 pt2 pt1]))

;; g a b g | c
(defn asc-pt-skip3-leap-to [{:keys [key previous-melody]}]
  (let [pt3 (prev-diatonic key previous-melody)
        pt2 (prev-diatonic key pt3)
        pt1 (prev-diatonic key pt2)
        pt4 pt1]
    [pt4 pt3 pt2 pt1]))

;; g a b g | a
;; b c d b | c
(defn asc-pt-skip3-to [{:keys [key previous-melody]}]
  (let [pt4 (prev-diatonic key previous-melody)
        pt3 (next-diatonic key previous-melody)
        pt2 (prev-diatonic key pt3) ;; previous-melody
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

;; consonant check 1 2
;; d g a b | c
(defn skip-4-asc-pt-to [{:keys [key previous-melody]}]
  (let [pt4 (prev-diatonic key previous-melody)
        pt3 (prev-diatonic key pt4)
        pt2 (prev-diatonic key pt3)
        pt1 (next-diatonic key previous-melody)]
    [pt4 pt3 pt2 pt1]))

;; b g a b | c
(defn skip-3-asc-pt-to [{:keys [key previous-melody]}]
  (let [pt4 (prev-diatonic key previous-melody)
        pt3 (prev-diatonic key pt4)
        pt2 (prev-diatonic key pt3)
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

;; d c b d | c
(defn desc-pt-skip-3-to [{:keys [key previous-melody]}]
  (let [pt4 (next-diatonic key previous-melody)
        pt3 (prev-diatonic key previous-melody)
        pt2 (next-diatonic key pt3) ;; previous-melody
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

;;  f e d b | c
(defn desc-pt-skip-desc-3-to [{:keys [key previous-melody]}]
  (let [pt4 (prev-diatonic key previous-melody)
        pt3 (next-diatonic key previous-melody)
        pt2 (next-diatonic key pt3)
        pt1 (next-diatonic key pt2)]
    [pt4 pt3 pt2 pt1]))

;; c e f g | a
(defn skip-m6-asc-pt-to [{:keys [key previous-melody]}]
  (let [pt4 (prev-diatonic key previous-melody)
        pt3 (prev-diatonic key pt4)
        pt2 (prev-diatonic key pt3)
        pt1 (next-diatonic key (next-diatonic key previous-melody))]
    [pt4 pt3 pt2 pt1]))

;; d c a b | c
(defn cambiata-to [{:keys [key previous-melody]}]
  (let [pt4 (prev-diatonic key previous-melody)
        pt3 (prev-diatonic key pt4)
        pt2 (next-diatonic key pt4) ;; previous-melody
        pt1 (next-diatonic key pt2)]
    [pt4 pt3 pt2 pt1]))

;; e d c b | c
(defn desc-pt-neighbor-to [{:keys [key previous-melody]}]
  (let [pt4 (prev-diatonic key previous-melody)
        pt3 (next-diatonic key pt4) ;; previous-melody
        pt2 (next-diatonic key pt3)
        pt1 (next-diatonic key pt2)]
    [pt4 pt3 pt2 pt1]))

;;  d b c d | c
(defn diss-3-asc-neighbor-to [{:keys [key previous-melody]}]
  (let [pt4 (next-diatonic key previous-melody)
        pt3 (prev-diatonic key pt4) ;; previous-melody
        pt2 (prev-diatonic key pt3)
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

;;  b d c b | c
(defn diss-3-desc-neighbor-to [{:keys [key previous-melody]}]
  (let [pt4 (prev-diatonic key previous-melody)
        pt3 (next-diatonic key pt4) ;; previous-melody
        pt2 (next-diatonic key pt3)
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

(defn chord-pt-to [{:keys [key previous-melody cantus-note]}]
  (let [pt4 (next-diatonic key previous-melody)
        pt3 (next-diatonic key pt4)
        pt2 (note-at-melodic-interval pt3 P4-)
        pt1 pt3]
    [pt4 pt3 pt2 pt1]))

(defn octave-skip-high-to [{:keys [key previous-melody cantus-note]}]
  (let [pt4 (next-diatonic key previous-melody)
        pt3 (next-diatonic key pt4)
        pt2 (next-diatonic key pt3)
        pt1 (note-at-melodic-interval pt2 P8-)]
    [pt4 pt3 pt2 pt1]))

(defn octave-skip-low-to [{:keys [key previous-melody cantus-note]}]
  (let [pt4 (prev-diatonic key previous-melody)
        pt3 (prev-diatonic key pt4)
        pt2 (prev-diatonic key pt3)
        pt1 (note-at-melodic-interval pt2 P8)]
    [pt4 pt3 pt2 pt1]))


(defn ending-1 [{:keys [key previous-melody]}]
  (let [pt4 (note-at-melodic-interval previous-melody m2-)
        pt3 (note-at-melodic-interval pt4 M2-)
        pt2 (prev-diatonic key pt3)
        pt1 (prev-diatonic key pt2)]
    [pt4 pt3 pt2 pt1]))

(defn ending-1-e [{:keys [key previous-melody]}]
  (let [pt4 (note-at-melodic-interval previous-melody M2-)
        pt3 (note-at-melodic-interval pt4 M2-)
        pt2 (prev-diatonic key pt3)
        pt1 (prev-diatonic key pt2)]
    [pt4 pt3 pt2 pt1]))

(defn ending-2 [{:keys [key previous-melody]}]
  (let [pt4 (note-at-melodic-interval previous-melody m2-)
        pt3 (note-at-melodic-interval pt4 M2-)
        pt2 previous-melody
        pt1 (next-diatonic key pt2)]
    [pt4 pt3 pt2 pt1]))

(defn ending-M2 [{:keys [key previous-melody]}]
  (let [pt4 (next-diatonic key previous-melody)
        pt3 (next-diatonic key pt4)
        pt2 (next-diatonic key pt3)
        pt1 (next-diatonic key pt2)]
    [pt4 pt3 pt2 pt1]))

(defn ending-below [{:keys [key previous-melody]}]
  (let [pt4 (note-at-melodic-interval previous-melody m2-)
        pt3 (note-at-melodic-interval pt4 M2-)
        pt2 (prev-diatonic key pt3)
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

(defn ending-below-e [{:keys [key previous-melody]}]
  (let [pt4 (note-at-melodic-interval previous-melody M2-)
        pt3 (note-at-melodic-interval pt4 M2-)
        pt2 (prev-diatonic key pt3)
        pt1 (prev-diatonic key pt2)]
    [pt4 pt3 pt2 pt1]))

(def patterns [octave-skip-high-to
               octave-skip-low-to
               chord-pt-to
               asc-pt-skip3-leap-to
               asc-pt-skip3-to
               skip-4-asc-pt-to
               skip-3-asc-pt-to
               desc-pt-skip-3-to
               desc-pt-skip-desc-3-to
               skip-m6-asc-pt-to
               cambiata-to
               desc-pt-to
               asc-pt-to
              ;;  desc-asc-pt-to ;; create up-downs
              ;;  asc-desc-pt-to ;; create up-downs
               desc-pt-neighbor-to
               diss-3-desc-neighbor-to])

(defn- consonant-nth-fn [n]
  (fn [beats {:keys [position cantus-note]}]
    (harmonic-consonant?
     (simple-interval cantus-note (nth (reverse beats) (dec n)) position))))

(defn- not-direct-to-perfect-from-nth-fn [n]
  (fn [beats {:keys [:previous-melody
                         :previous-cantus
                         :cantus-note]}]
  (not (direct-perfect? cantus-note (nth (reverse beats) (dec n)) previous-cantus previous-melody))))

(defn- skip-m6-fn
  [[p4 p3 p2 p1] _]
  (= m6- (interval p1 p2)))

(defn- not-dim5-fn
  [[p4 p3 p2 p1] {:keys [previous-melody]}]
  (and (not= d5- (interval p1 p2))
      ;;  (not= A4 (interval p2 previous-melody))
       ))

(defn and-fn
  [& validation-fn-list]
  (fn [pattern state]
    (every?  #(% pattern state) validation-fn-list)))

(def patterns-validation-fn
  {cambiata-to (consonant-nth-fn 3)
   desc-pt-to (and-fn (consonant-nth-fn 3)
                      (not-direct-to-perfect-from-nth-fn 3))
   asc-pt-to (and-fn (consonant-nth-fn 3)
                     (not-direct-to-perfect-from-nth-fn 3))
   desc-asc-pt-to (and-fn (consonant-nth-fn 3)
                          (not-direct-to-perfect-from-nth-fn 3))
   asc-desc-pt-to (and-fn (consonant-nth-fn 3)
                          (not-direct-to-perfect-from-nth-fn 3))
   skip-4-asc-pt-to (and-fn (consonant-nth-fn 2)
                            not-dim5-fn
                            (not-direct-to-perfect-from-nth-fn 4))
   skip-3-asc-pt-to (and-fn (consonant-nth-fn 2)
                            (not-direct-to-perfect-from-nth-fn 2))
   asc-pt-skip3-leap-to (and-fn (consonant-nth-fn 3)
                                (not-direct-to-perfect-from-nth-fn 1))
   asc-pt-skip3-to (and-fn (consonant-nth-fn 3)
                           (not-direct-to-perfect-from-nth-fn 1)
                           (not-direct-to-perfect-from-nth-fn 3))
   skip-m6-asc-pt-to (and-fn (consonant-nth-fn 2)
                             skip-m6-fn)
   desc-pt-skip-3-to (and-fn (consonant-nth-fn 3)
                             (not-direct-to-perfect-from-nth-fn 1)
                             (not-direct-to-perfect-from-nth-fn 3))
   desc-pt-skip-desc-3-to (and-fn (consonant-nth-fn 3)
                                  (consonant-nth-fn 4)
                                  (not-direct-to-perfect-from-nth-fn 4))
   desc-pt-neighbor-to (not-direct-to-perfect-from-nth-fn 4)
   diss-3-desc-neighbor-to (and-fn (consonant-nth-fn 2)
                                   (consonant-nth-fn 4)
                                   (not-direct-to-perfect-from-nth-fn 4)
                                   (not-direct-to-perfect-from-nth-fn 2))
   chord-pt-to (and-fn (consonant-nth-fn 3)
                       (consonant-nth-fn 2)
                       (not-direct-to-perfect-from-nth-fn 3)
                       (not-direct-to-perfect-from-nth-fn 4))})



(defn pattern-fits?
  [pattern
   {:keys [position key previous-melody cantus-note m36s melody]
    :as state}]
  (let [[p4 p3 p2 p1 :as applied] (pattern state)]
    [applied
     (and
      (or (> (get m36s :remaining-cantus-size) 1)
          (= position :above)
          (= P8 (interval p1 cantus-note))
          (= p1 cantus-note))
      (or (> (get m36s :remaining-cantus-size) 1)
          (= position :below)
          (= P8 (interval cantus-note p1))
          (= P5 (interval cantus-note p1))
          (= p1 cantus-note))
      (harmonic-consonant? (simple-interval cantus-note p1 position))
      ((get patterns-validation-fn pattern (fn [_ _] true))
       applied state)
      (or
       (= (get m36s :remaining-cantus-size) 1)
       ((crossing-filter position cantus-note false) p1))
      (every? #((crossing-filter position cantus-note true) %) [p4 p3 p2])
      (every?
       #(<= (Math/abs (get-interval (interval cantus-note %))) max-harmonic-interval)
       applied)
      (maximum-range-M10? (append-to-melody melody [p4 p3 p2 p1]))
      ((dim-or-aug-filter position p4) previous-melody))]))


;; (->> next-melodic-candidates
;;      (filter #(or (get options :melodic-unison-allowed)
;;                   (not= previous-melody %)))
;;      (filter #(or (get options :harmonic-unison-allowed)
;;                   (not= cantus-note %)))
;;      (debug "no rep")
;;      (filter (dim-or-aug-filter position cantus-note))
;;      (debug "no dim aug")
;;      (filter (crossing-filter position cantus-note))
;;      (debug "no crossing")
;;      (filter #((set next-harmonic-candidates) (get-nooctave %)))
;;      (debug "consonant")
;;      (filter #(maximum-range-M10? (append-to-melody melody [%])))
;;      (filter #(<= (Math/abs (get-interval (interval cantus-note %))) max-harmonic-interval))

;;                             ;;  haydn-a is not solvable
;;                              ;; is this examined twice??
;;      (filter #(not (direct-motion-to-perfect? cantus-note % previous-cantus previous-melody))))