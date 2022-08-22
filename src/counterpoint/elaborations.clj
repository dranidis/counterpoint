(ns counterpoint.elaborations
  (:require [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.generate-first-species :refer [dim-or-aug-filter]]
            [counterpoint.intervals :refer [d5 d5- diatonic get-interval
                                            harmonic-consonant? M2- next-diatonic
                                            note-at-melodic-interval prev-diatonic]]
            [counterpoint.utils :refer [remove-last]]))

(defn- suspension-embelishment-anticipate-res
  [_ res diss]
  [{:d 2 :n [res]} {:d 4 :n [res diss]}])

(defn- suspension-embelishment-repeat-diss
  [_ res diss]
  [{:d 2 :n [res]} {:d 4 :n [diss diss]}])

(defn- suspension-embelishment-down-leap
  [{:keys [key position cantus-note bar-melody previous-melody]} res diss]
  (let [last? (= 1 (count bar-melody))
        ascending? (> (get-interval (interval res previous-melody)) 0)]
    (if (or last? ascending?)
      nil
      (let [diatonic-notes-lower-than-res (map #(diatonic key res %)
                                               [-3 -4 -5])
            consonant (->> diatonic-notes-lower-than-res
                           (filter
                            #(harmonic-consonant?
                              (simple-interval cantus-note % position)))
                           (filter
                            (dim-or-aug-filter position res)))
            down-leap (first consonant)]
        (if (nil? down-leap)
          nil
          [{:d 2 :n [res]} {:d 4 :n [down-leap diss]}])))))

(defn- suspension-embelishment-two-eight-notes
  [{:keys [key bar-melody]} res diss]
  (let [last? (= 1 (count bar-melody))
        prev (if last?
               (note-at-melodic-interval res M2-)
               (prev-diatonic key res))]
    [{:d 2 :n [res]} {:d 8 :n [prev res]} {:d 4 :n [diss]}]))

(defn- suspension-mozart-embelishment-skip-up-4-eight-notes
  [{:keys [key position cantus-note]} res diss]
  (let [n3 (next-diatonic key res)
        n4 (next-diatonic key n3)
        n5 (next-diatonic key n4)
        consonant? (harmonic-consonant?
                    (simple-interval cantus-note n5 position))
        no-dim? (not= d5 (interval diss n5))]
    (if (and consonant? no-dim?)
      [{:d 2 :n [res]} {:d 8 :n [n3 n4 n5 diss]}]
      nil)))

(defn- suspension-mozart-embelishment-skip-down-4-eight-notes
  [{:keys [key position cantus-note bar-melody]} res diss]
  (let [last? (= 1 (count bar-melody))
        prev (if last?
               (note-at-melodic-interval res M2-)
               (prev-diatonic key res))
        n4 (prev-diatonic key prev)
        n5 (prev-diatonic key n4)
        consonant? (harmonic-consonant?
                    (simple-interval cantus-note n5 position))
        no-dim? (not= d5- (interval diss n5))]
    (if (and consonant? no-dim?)
      [{:d 2 :n [res]} {:d 8 :n [prev n4 n5 diss]}]
      nil)))

(def suspension-embelishments-fn
  [suspension-embelishment-anticipate-res
   suspension-embelishment-two-eight-notes
   suspension-embelishment-down-leap
   suspension-embelishment-repeat-diss
   suspension-mozart-embelishment-skip-down-4-eight-notes
   suspension-mozart-embelishment-skip-up-4-eight-notes])

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
                             previous-melody bar-melody
                             rest-cantus]
                      :as state}
                     [{:keys [d n] :as bar}]]
  ;; (println "elaborate 4th KEY" key)
  (when (not= 2 d)
    (throw (Exception. (str "elaborate-4th: wrong duration :d" d))))
  (if (< (count rest-cantus) 1)
    [bar]
    (let [[up-beat dn-beat] n
          dn-up-interval (interval dn-beat up-beat)
          up-next-bar-interval (interval up-beat previous-melody)
          first-beat-emb
          (if (= :rest-interval dn-up-interval)
            [bar]
            (case (get-interval dn-up-interval)
              -2 (if (harmonic-consonant? (simple-interval
                                           cantus-note dn-beat position))
                   [bar]
                   (let [suspensions
                         (map
                          (fn [emb-fn] (emb-fn state up-beat dn-beat))
                          suspension-embelishments-fn)
                         applicable (remove nil? suspensions)]
                     (nth applicable (mod (count bar-melody)
                                          (count applicable)))))
              -3 (third-diminution-low key up-beat dn-beat)
              3 (third-diminution-high key up-beat dn-beat)
              -4 (fourth-diminution-low key up-beat dn-beat)
              4 (fourth-diminution-high key up-beat dn-beat)
              [bar]))
          second-beat-emb
          (case (get-interval up-next-bar-interval)
            4 (fourth-diminution-to-previous
               key first-beat-emb previous-melody)
            -4 (fourth-diminution-to-previous
                key first-beat-emb previous-melody)
            3 (third-diminution-to-previous
               key first-beat-emb previous-melody)
            -3 (third-diminution-to-previous
                key first-beat-emb previous-melody)
            first-beat-emb)]
      second-beat-emb)))

(defn elaborate-suspension-with-next-working-bar
  "Adds a suspension when the previous bar note is a 2nd higher.
   The first note of the bar needs to be a half (duration)"
  [elaborated-bar prev-working-bar]
  ;; IMPORTANT prev bar has not been elaborated YET!!!
  ;; the last note is the 4th species last note
  (let [last-note-of-prev (first (:n (first prev-working-bar)))
        first-note-of-current (last (:n (last elaborated-bar)))
        desc-2nd? (= -2 (get-interval (interval
                                       last-note-of-prev
                                       first-note-of-current)))
        duration-2? (= 2 (:d (last elaborated-bar)))
        alone-in-the-list? (= 1 (count (:n (last elaborated-bar))))
        result (if (and desc-2nd? duration-2?)
                 (if alone-in-the-list?
                   (conj (remove-last elaborated-bar)
                         {:d 4 :n [first-note-of-current last-note-of-prev]})
                   (into (remove-last elaborated-bar)
                         (conj [{:d 2 :n (remove-last (:n (last elaborated-bar)))}]
                               {:d 4 :n [first-note-of-current last-note-of-prev]})))
      ;; elaborated-bar
                 elaborated-bar)]
    ;; (when (and desc-2nd? duration-2?)
      ;; (println "LAST NOTE OF PREV BAR" last-note-of-prev)
      ;; (println "FIRST NOTE OF CURR BAR" first-note-of-current)
      ;; (println desc-2nd?)
      ;; (println alone-in-the-list?)
      ;; (println "PREV" prev-working-bar)
      ;; (println "WAS" elaborated-bar)
      ;; (println "IS" result))
    result))

