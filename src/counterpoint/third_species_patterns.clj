(ns counterpoint.third-species-patterns
  (:require [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.generate-first-species :refer [crossing-filter
                                                         max-harmonic-interval]]
            [counterpoint.intervals :refer [get-interval harmonic-consonant?
                                            m2- M2- next-diatonic
                                            note-at-melodic-interval prev-diatonic]]))

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
        pt1 note]
    [pt4 pt3 pt2 pt1]))

(defn asc-desc-pt-to [key-sig note]
  (let [pt4 (next-diatonic key-sig note)
        pt3 (next-diatonic key-sig pt4)
        pt2 pt4
        pt1 note]
    [pt4 pt3 pt2 pt1]))

;; g a b g | c
(defn asc-pt-skip3-leap-to [key-sig note]
  (let [pt3 (prev-diatonic key-sig note)
        pt2 (prev-diatonic key-sig pt3)
        pt1 (prev-diatonic key-sig pt2)
        pt4 pt1]
    [pt4 pt3 pt2 pt1]))

;; g a b g | a
(defn asc-pt-skip3-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (next-diatonic key-sig note)
        pt2 note
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

(defn desc-pt-skip-3-to [key-sig note]
  (let [pt4 (next-diatonic key-sig note)
        pt3 (prev-diatonic key-sig note)
        pt2 note
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

(defn desc-pt-skip-desc-3-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (next-diatonic key-sig note)
        pt2 (next-diatonic key-sig pt3)
        pt1 (next-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

(defn skip-m6-asc-pt-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (prev-diatonic key-sig pt4)
        pt2 (prev-diatonic key-sig pt3)
        pt1 (next-diatonic key-sig (next-diatonic key-sig note))]
    [pt4 pt3 pt2 pt1]))


(defn cambiata-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 (prev-diatonic key-sig pt4)
        pt2 note
        pt1 (next-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

(defn desc-pt-neighbor-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 note
        pt2 (next-diatonic key-sig pt3)
        pt1 (next-diatonic key-sig pt2)]
    [pt4 pt3 pt2 pt1]))

(defn diss-3-asc-neighbor-to [key-sig note]
  (let [pt4 (next-diatonic key-sig note)
        pt3 note
        pt2 (prev-diatonic key-sig pt3)
        pt1 pt4]
    [pt4 pt3 pt2 pt1]))

(defn diss-3-desc-neighbor-to [key-sig note]
  (let [pt4 (prev-diatonic key-sig note)
        pt3 note
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

(def patterns [cambiata-to
               desc-pt-to
               asc-pt-to
               desc-asc-pt-to
               asc-desc-pt-to
               asc-pt-skip3-leap-to
               asc-pt-skip3-to
               skip-4-asc-pt-to
               skip-3-asc-pt-to
               desc-pt-skip-3-to
               desc-pt-skip-desc-3-to
               skip-m6-asc-pt-to
               desc-pt-neighbor-to
               diss-3-desc-neighbor-to])

(defn- consonant-3rd-fn  [[p4 p3 p2 p1]
                          {:keys [position cantus-note]}]
  (harmonic-consonant?
   (simple-interval cantus-note p3 position)))

(defn- consonant-2nd-fn  [[p4 p3 p2 p1]
                          {:keys [position cantus-note]}]
  (harmonic-consonant?
   (simple-interval cantus-note p2 position)))

(def patterns-validation-fn
  {cambiata-to consonant-3rd-fn
   desc-pt-to consonant-3rd-fn
   asc-pt-to consonant-3rd-fn
   desc-asc-pt-to consonant-3rd-fn
   asc-desc-pt-to consonant-3rd-fn
   skip-4-asc-pt-to consonant-2nd-fn
   skip-3-asc-pt-to consonant-2nd-fn
   asc-pt-skip3-leap-to consonant-3rd-fn})

(defn pattern-fits?
  [pattern
   {:keys [position key previous-melody cantus-note]
    :as state}]
  (let [[p4 p3 p2 p1 :as applied] (pattern key previous-melody)]
    [applied
     (and
      (harmonic-consonant? (simple-interval cantus-note p1 position))
      ((get patterns-validation-fn pattern (fn [_ _] true))
       applied state)
      ((crossing-filter position cantus-note false) p1)
      (every? #((crossing-filter position cantus-note true) %) [p4 p3 p2])
      (every?
       #(<= (Math/abs (get-interval (interval cantus-note %))) max-harmonic-interval)
       applied))]))


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