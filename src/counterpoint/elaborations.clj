(ns counterpoint.elaborations
  (:require [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.intervals :refer [diatonic get-interval
                                            harmonic-consonant? M2- next-diatonic
                                            note-at-melodic-interval prev-diatonic]]))

(defn- suspension-embelishment-1 [key-sig n1 n2]
  [{:d 2 :n [n1]}
   {:d 4 :n [n1 n2]}])

(defn- suspension-embelishment-2 [key-sig cantus-note n1 n2]
  (let [notes (map #(diatonic key-sig n1 %) [-3 -4 -5 -6])
        consonant (filter
                   #(harmonic-consonant? (simple-interval cantus-note %))
                   notes)
        n3 (first consonant)]

    (if (nil? n3)
      (suspension-embelishment-1 key-sig n1 n2)
      [{:d 2 :n [n1]}
       {:d 4 :n [n3 n2]}])))

(defn- suspension-embelishment-3 [key-sig n1 n2 bar-melody]
  (let [last? (= 1 (count bar-melody))
        n3 (if last?
             (note-at-melodic-interval n1 M2-)
             (prev-diatonic key-sig n1))]
    [{:d 2 :n [n1]}
     {:d 8 :n [n3 n1]}
     {:d 4 :n [n2]}]))

(defn- third-diminution-low [key-sig n1 n2]
  (let [n3 (prev-diatonic key-sig n2)]
    [{:d 2 :n [n1]}
     {:d 4 :n [n3 n2]}]))

(defn- third-diminution-high [key-sig n1 n2]
  (let [n3 (next-diatonic key-sig n2)]
    [{:d 2 :n [n1]}
     {:d 4 :n [n3 n2]}]))

(defn- fourth-diminution-high [key-sig n1 n2]
  (let [n3 (next-diatonic key-sig n2)
        n4 (next-diatonic key-sig n3)]
    [{:d 2 :n [n1]}
     {:d 8 :n [n4 n3]}
     {:d 4 :n [n2]}]))

(defn- fourth-diminution-low [key-sig n1 n2]
  (let [n3 (prev-diatonic key-sig n2)
        n4 (prev-diatonic key-sig n3)]
    [{:d 2 :n [n1]}
     {:d 8 :n [n4 n3]}
     {:d 4 :n [n2]}]))

(defn fourth-diminution-high-to-previous [key-sig first-beat-emb previous-melody]
  ;; (println "4 high to previous" first-beat-emb " TO " previous-melody)
  (let [last-2nd (first first-beat-emb)
        alone-in-the-list? (= 1 (count (:n last-2nd)))
        ;; _ (println "LAST 2nd" last-2nd) 
        note-from (first (:n last-2nd))
        n81 (next-diatonic key-sig note-from)
        n82 (next-diatonic key-sig n81)]
    (if alone-in-the-list?
      (into [{:d 8 :n [n82 n81]}
           {:d 4 :n [note-from]}]
          (rest first-beat-emb))
      (into (into [{:d 8 :n [n82 n81]}
             {:d 4 :n [note-from]}]
            [{:d 2 :n (rest (:n last-2nd))}])
            (rest first-beat-emb)
            ))))

(defn- fourth-diminution-low-to-previous [key-sig first-beat-emb previous-melody]
  (let [last-2nd (first first-beat-emb)
        alone-in-the-list? (= 1 (count (:n last-2nd)))
        ;; _ (println "LAST 2nd" last-2nd) 
        note-from (first (:n last-2nd))
        n81 (prev-diatonic key-sig note-from)
        n82 (prev-diatonic key-sig n81)]
    (if alone-in-the-list?
      (into [{:d 8 :n [n82 n81]}
             {:d 4 :n [note-from]}]
            (rest first-beat-emb))
      (into (into [{:d 8 :n [n82 n81]}
                   {:d 4 :n [note-from]}]
                  [{:d 2 :n (rest (:n last-2nd))}])
            (rest first-beat-emb)))))

(defn elaborate-4th [{:keys [key position cantus-note
                             previous-melody bar-melody]} [{:keys [d n]
                                                            :as all}]]
  (when (not= 2 d)
    (throw (Exception. (str "elaborate-4th: wrong duration :d" d))))
  (let [[n1 n2] n
        int-val (interval n2 n1)
        int-val-next (interval n1 previous-melody)
        _ (println "INTVAL NEXT" int-val-next)
        first-beat-emb (if (= :rest-interval int-val)
                         [all]
                         (case (get-interval int-val)
                           -2 (if (harmonic-consonant? (simple-interval
                                                        cantus-note n2 position))
                                [all]
                                (case (mod (count bar-melody) 3)
                                  0 (suspension-embelishment-1 key n1 n2)
                                  2 (suspension-embelishment-2 key cantus-note n1 n2)
                                  1 (suspension-embelishment-3 key n1 n2 bar-melody)))
                           -3 (third-diminution-low key n1 n2)
                           3 (third-diminution-high key n1 n2)
                           -4 (fourth-diminution-low key n1 n2)
                           4 (fourth-diminution-high key n1 n2)
                           [all]))
        second-beat-emb (case (get-interval int-val-next)
                          4 (fourth-diminution-high-to-previous
                             key first-beat-emb previous-melody)
                          -4 (fourth-diminution-low-to-previous
                             key first-beat-emb previous-melody)
                          first-beat-emb)]
    second-beat-emb))


