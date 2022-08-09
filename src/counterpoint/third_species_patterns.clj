(ns counterpoint.third-species-patterns
  (:require [counterpoint.cantus :refer [maximum-range-M10?]]
            [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.generate-first-species :refer [crossing-filter
                                                         dim-or-aug-filter
                                                         max-harmonic-interval]]
            [counterpoint.intervals :refer [d5- get-interval
                                            harmonic-consonant? m2- M2- m6- next-diatonic
                                            note-at-melodic-interval P5 P8 prev-diatonic]]
            [counterpoint.melody :refer [append-to-melody]]
            [counterpoint.motion :refer [direct-perfect?]]))

(defn asc-pt-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (prev-diatonic key-sig pt4)
        pt2 (prev-diatonic key-sig pt3)
        pt1 (prev-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

(defn desc-pt-to [key-sig note]
  (let [pt4 (next-diatonic key-sig note)
        pt3 (next-diatonic key-sig pt4)
        pt2 (next-diatonic key-sig pt3)
        pt1 (next-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

;;  c b a b | c
(defn desc-asc-pt-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (prev-diatonic key-sig pt4)
        pt2 pt4
        pt1 (next-diatonic key-sig pt2) ;; note
        ]
    [pt4 pt3 pt2 pt1]))

;; c d e d | c
(defn asc-desc-pt-to [key-sig note]
  (let [pt4 (next-diatonic key-sig note)
        pt3 (next-diatonic key-sig pt4)
        pt2 pt4
        pt1 (prev-diatonic key-sig pt2) ;; note
        ]
    [pt4 pt3 pt2 pt1]))

;; g a b g | c
(defn asc-pt-skip3-leap-to [key-sig note]
  (let [pt3 (prev-diatonic key-sig note)
        pt2 (prev-diatonic key-sig pt3)
        pt1 (prev-diatonic key-sig pt2)
        pt4 pt1]
    [pt4 pt3 pt2 pt1]))

;; g a b g | a
;; b c d b | c
(defn asc-pt-skip3-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (next-diatonic key-sig note)
        pt2 (prev-diatonic key-sig pt3) ;; note
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

;; consonant check 1 2
;; d g a b | c
(defn skip-4-asc-pt-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (prev-diatonic key-sig pt4)
        pt2 (prev-diatonic key-sig pt3)
        pt1 (next-diatonic key-sig note)]
    [pt4 pt3 pt2 pt1]))

;; b g a b | c
(defn skip-3-asc-pt-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (prev-diatonic key-sig pt4)
        pt2 (prev-diatonic key-sig pt3)
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

;; d c b d | c
(defn desc-pt-skip-3-to [key-sig note]
  (let [pt4 (next-diatonic key-sig note)
        pt3 (prev-diatonic key-sig note)
        pt2 (next-diatonic key-sig pt3) ;; note
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

;;  f e d b | c
(defn desc-pt-skip-desc-3-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (next-diatonic key-sig note)
        pt2 (next-diatonic key-sig pt3)
        pt1 (next-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

;; c e f g | a
(defn skip-m6-asc-pt-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (prev-diatonic key-sig pt4)
        pt2 (prev-diatonic key-sig pt3)
        pt1 (next-diatonic key-sig (next-diatonic key-sig note))]
    [pt4 pt3 pt2 pt1]))

;; d c a b | c
(defn cambiata-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (prev-diatonic key-sig pt4)
        pt2 (next-diatonic key-sig pt4) ;; note
        pt1 (next-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

;; e d c b | c
(defn desc-pt-neighbor-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (next-diatonic key-sig pt4) ;; note
        pt2 (next-diatonic key-sig pt3)
        pt1 (next-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

;;  d b c d | c
(defn diss-3-asc-neighbor-to [key-sig note]
  (let [pt4 (next-diatonic key-sig note)
        pt3 (prev-diatonic key-sig pt4) ;; note
        pt2 (prev-diatonic key-sig pt3)
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

;;  b d c b | c
(defn diss-3-desc-neighbor-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (next-diatonic key-sig pt4) ;; note
        pt2 (next-diatonic key-sig pt3)
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

(defn ending-1 [key-sig note]
  (let [pt4 (note-at-melodic-interval note m2-)
        pt3 (note-at-melodic-interval pt4 M2-)
        pt2 (prev-diatonic key-sig pt3)
        pt1 (prev-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

(defn ending-1-e [key-sig note]
  (let [pt4 (note-at-melodic-interval note M2-)
        pt3 (note-at-melodic-interval pt4 M2-)
        pt2 (prev-diatonic key-sig pt3)
        pt1 (prev-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

(defn ending-2 [key-sig note]
  (let [pt4 (note-at-melodic-interval note m2-)
        pt3 (note-at-melodic-interval pt4 M2-)
        pt2 note
        pt1 (next-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

(defn ending-M2 [key-sig note]
  (let [pt4 (next-diatonic key-sig note)
        pt3 (next-diatonic key-sig pt4)
        pt2 (next-diatonic key-sig pt3)
        pt1 (next-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

(defn ending-below [key-sig note]
  (let [pt4 (note-at-melodic-interval note m2-)
        pt3 (note-at-melodic-interval pt4 M2-)
        pt2 (prev-diatonic key-sig pt3)
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

(defn ending-below-e [key-sig note]
  (let [pt4 (note-at-melodic-interval note M2-)
        pt3 (note-at-melodic-interval pt4 M2-)
        pt2 (prev-diatonic key-sig pt3)
        pt1 (prev-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

(def patterns [asc-pt-skip3-leap-to
               asc-pt-skip3-to
               skip-4-asc-pt-to
               skip-3-asc-pt-to
               desc-pt-skip-3-to
               desc-pt-skip-desc-3-to
               skip-m6-asc-pt-to
               cambiata-to
               desc-pt-to
               asc-pt-to
               desc-asc-pt-to
               asc-desc-pt-to
               desc-pt-neighbor-to
               diss-3-desc-neighbor-to])

(defn- consonant-4th-fn  [[p4 p3 p2 p1]
                          {:keys [position cantus-note]}]
  (harmonic-consonant?
   (simple-interval cantus-note p4 position)))

(defn- consonant-3rd-fn  [[p4 p3 p2 p1]
                          {:keys [position cantus-note]}]
  (harmonic-consonant?
   (simple-interval cantus-note p3 position)))

(defn- consonant-2nd-fn  [[p4 p3 p2 p1]
                          {:keys [position cantus-note]}]
  (harmonic-consonant?
   (simple-interval cantus-note p2 position)))

(defn- not-direct-to-perfect-from-3-fn
  [[p4 p3 p2 p1] {:keys [:previous-melody
                         :previous-cantus
                         :cantus-note]}]
  (not (direct-perfect? cantus-note p3 previous-cantus previous-melody)))

(defn- not-direct-to-perfect-from-4-fn
  [[p4 p3 p2 p1] {:keys [:previous-melody
                         :previous-cantus
                         :cantus-note]}]
  (not (direct-perfect? cantus-note p4 previous-cantus previous-melody)))

(defn- not-direct-to-perfect-from-1-fn
  [[p4 p3 p2 p1] {:keys [:previous-melody
                         :previous-cantus
                         :cantus-note]}]
  (not (direct-perfect? cantus-note p1 previous-cantus previous-melody)))

(defn- not-direct-to-perfect-from-2-fn
  [[p4 p3 p2 p1] {:keys [:previous-melody
                         :previous-cantus
                         :cantus-note]}]
  (not (direct-perfect? cantus-note p2 previous-cantus previous-melody)))

(defn- skip-m6-fn
  [[p4 p3 p2 p1] _]
  (= m6- (interval p1 p2)))

(defn- not-dim5-fn
  [[p4 p3 p2 p1] _]
  (not= d5- (interval p1 p2)))

(defn and-fn
  ([fn1 fn2] (and-fn fn1 fn2 nil nil))
  ([fn1 fn2 fn3] (and-fn fn1 fn2 fn3 nil))
  ([fn1 fn2 fn3 fn4]
   (fn [pattern state]
     (and (fn1 pattern state)
          (fn2 pattern state)
          (or (nil? fn3) (fn3 pattern state))
          (or (nil? fn4) (fn4 pattern state))))))

(def patterns-validation-fn
  {cambiata-to consonant-3rd-fn
   desc-pt-to (and-fn consonant-3rd-fn
                      not-direct-to-perfect-from-3-fn)
   asc-pt-to (and-fn consonant-3rd-fn
                     not-direct-to-perfect-from-3-fn)
   desc-asc-pt-to (and-fn consonant-3rd-fn
                          not-direct-to-perfect-from-3-fn)
   asc-desc-pt-to (and-fn consonant-3rd-fn
                          not-direct-to-perfect-from-3-fn)
   skip-4-asc-pt-to (and-fn consonant-2nd-fn
                            not-dim5-fn
                            not-direct-to-perfect-from-4-fn)
   skip-3-asc-pt-to (and-fn consonant-2nd-fn
                            not-direct-to-perfect-from-2-fn)
   asc-pt-skip3-leap-to (and-fn consonant-3rd-fn
                                not-direct-to-perfect-from-1-fn)
   asc-pt-skip3-to (and-fn consonant-3rd-fn
                           not-direct-to-perfect-from-1-fn
                           not-direct-to-perfect-from-3-fn)
   skip-m6-asc-pt-to (and-fn consonant-2nd-fn
                             skip-m6-fn)
   desc-pt-skip-3-to (and-fn consonant-3rd-fn
                             not-direct-to-perfect-from-1-fn
                             not-direct-to-perfect-from-3-fn)
   desc-pt-skip-desc-3-to (and-fn consonant-3rd-fn
                                  consonant-4th-fn
                                  not-direct-to-perfect-from-4-fn)
   desc-pt-neighbor-to not-direct-to-perfect-from-4-fn
   diss-3-desc-neighbor-to (and-fn consonant-2nd-fn
                                   consonant-4th-fn
                                   not-direct-to-perfect-from-4-fn
                                   not-direct-to-perfect-from-2-fn)})



(defn pattern-fits?
  [pattern
   {:keys [position key previous-melody cantus-note m36s melody]
    :as state}]
  (let [[p4 p3 p2 p1 :as applied] (pattern key previous-melody)]
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
      (every? #((crossing-filter position cantus-note false) %) [p4 p3 p2])
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