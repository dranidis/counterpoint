(ns counterpoint.elaborations
  (:require [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.intervals :refer [diatonic get-interval
                                            harmonic-consonant? M2- next-diatonic
                                            note-at-melodic-interval prev-diatonic]]))

(defn- suspension-embelishment-1 [key-sig n1 n2]
  [{:d 2 :n [n1]}
   {:d 4 :n [n1 n2]}])

(defn- suspension-embelishment-2 [key-sig position cantus-note n1 n2]
  (let [notes (map #(diatonic key-sig n1 %) [-3 -4 -5 -6])
        consonant (filter
                   #(harmonic-consonant? (simple-interval cantus-note % position))
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

(defn- diminution [key-sig note-from previous-melody]
  (let [high? (> (get-interval (interval note-from previous-melody)) 0)
        next-fn (if high? next-diatonic prev-diatonic)
        n81 (next-fn key-sig note-from)
        n82 (next-fn key-sig n81)]
    [n82 n81]))

(defn fourth-diminution-to-previous [key-sig first-beat-emb previous-melody]
  ;; (println "4 high to previous" first-beat-emb " TO " previous-melody)
  (let [last-2nd (first first-beat-emb)
        alone-in-the-list? (= 1 (count (:n last-2nd)))
        ;; _ (println "LAST 2nd" last-2nd) 
        note-from (first (:n last-2nd))
        [n82 n81] (diminution key-sig note-from previous-melody)]
    (if alone-in-the-list?
      (into [{:d 8 :n [n82 n81]}
             {:d 4 :n [note-from]}]
            (rest first-beat-emb))
      (into (into [{:d 8 :n [n82 n81]}
                   {:d 4 :n [note-from]}]
                  [{:d 2 :n (rest (:n last-2nd))}])
            (rest first-beat-emb)))))

(defn- third-diminution-to-previous [key first-beat-emb previous-melody]
  (let [last-2nd (first first-beat-emb)
        alone-in-the-list? (= 1 (count (:n last-2nd)))
        ;; _ (println "LAST 2nd" last-2nd) 
        note-from (first (:n last-2nd))
        high? (> (get-interval (interval note-from previous-melody)) 0)
        n4 ((if high? next-diatonic prev-diatonic) key note-from)]
    (if alone-in-the-list?
      (into [{:d 4 :n [n4 note-from]}]
            (rest first-beat-emb))
      (into (into [{:d 4 :n [n4 note-from]}]
                  [{:d 2 :n (rest (:n last-2nd))}])
            (rest first-beat-emb)))))

;; (defn- fourth-diminution-to-previous [key-sig first-beat-emb previous-melody]
;;   (let [last-2nd (first first-beat-emb)
;;         alone-in-the-list? (= 1 (count (:n last-2nd)))
;;         ;; _ (println "LAST 2nd" last-2nd) 
;;         note-from (first (:n last-2nd))
;;         n81 (prev-diatonic key-sig note-from)
;;         n82 (prev-diatonic key-sig n81)]
;;     (if alone-in-the-list?
;;       (into [{:d 8 :n [n82 n81]}
;;              {:d 4 :n [note-from]}]
;;             (rest first-beat-emb))
;;       (into (into [{:d 8 :n [n82 n81]}
;;                    {:d 4 :n [note-from]}]
;;                   [{:d 2 :n (rest (:n last-2nd))}])
;;             (rest first-beat-emb)))))

(defn elaborate-4th [{:keys [key position cantus-note
                             previous-melody bar-melody]}
                     [{:keys [d n] :as bar}]]
  (when (not= 2 d)
    (throw (Exception. (str "elaborate-4th: wrong duration :d" d))))
  (let [[n1 n2] n
        int-val (interval n2 n1)
        int-val-next (interval n1 previous-melody)
        first-beat-emb (if (= :rest-interval int-val)
                         [bar]
                         (case (get-interval int-val)
                           -2 (if (harmonic-consonant? (simple-interval
                                                        cantus-note n2 position))
                                [bar]
                                (case (mod (count bar-melody) 2)
                                  ;; 0 (suspension-embelishment-1 key n1 n2)
                                  0 (suspension-embelishment-2 key position cantus-note n1 n2)
                                  1 (suspension-embelishment-3 key n1 n2 bar-melody)))
                           -3 (third-diminution-low key n1 n2)
                           3 (third-diminution-high key n1 n2)
                           -4 (fourth-diminution-low key n1 n2)
                           4 (fourth-diminution-high key n1 n2)
                           [bar]))
        second-beat-emb (case (get-interval int-val-next)
                          4 (fourth-diminution-to-previous
                             key first-beat-emb previous-melody)
                          -4 (fourth-diminution-to-previous
                              key first-beat-emb previous-melody)
                          3 (third-diminution-to-previous
                             key first-beat-emb previous-melody)
                          -3 (third-diminution-to-previous
                              key first-beat-emb previous-melody)
                          first-beat-emb)]
    second-beat-emb))

