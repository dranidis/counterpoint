(ns counterpoint.melody 
  (:require [counterpoint.core :refer [interval note->number-of-semitones]]
            [counterpoint.notes :as notes]))

(defn make-melody [note & notes]
  (into [note] notes))

(defn- lowest-note [melody]
  (second (apply min-key #(note->number-of-semitones (second %)) (map-indexed vector melody)))
  )

(defn- highest-note [melody]
  (second (apply max-key #(note->number-of-semitones (second %)) (map-indexed vector melody)))
  )

(defn melody-range [melody]
  (interval (lowest-note melody)
            (highest-note melody)))

(comment
  (def melody (make-melody notes/a4 notes/c3 notes/e3 notes/e4 notes/b4 notes/a4))
  (def melody (make-melody notes/a4 notes/c3 notes/e3 notes/e4 notes/g#3 notes/a4))

  (lowest-note melody)
  (highest-note melody)
  (melody-range melody)
  melody


  (nth melody (dec (dec (count melody))))

  (def final-interval (interval (nth melody (dec (dec (count melody))))
                            (last melody))
)




  ;

  )
