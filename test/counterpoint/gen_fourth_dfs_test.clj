(ns counterpoint.gen-fourth-dfs-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [get-melody]]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.fourth-species :refer [evaluate-fourth-species
                                                 fourth-species-rules?
                                                 make-fourth-species]]
            [counterpoint.gen-fourth-dfs :refer [candidates next-node-4th
                                                 second-to-last-measure-candidates-4th]]
            [counterpoint.generate :refer [generate-reverse-counterpoint-dfs]]
            [counterpoint.lilypond :refer [species-coll->lily]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.utils :refer [dfs-solution->cp]]))


(deftest candidates-test
  (testing "2nd-to-last-incomplete-test"
    (let [position :above
          previous-melody n/d5
          previous-cantus n/d4
          cantus-note n/e4
          next-cantus n/f4
          sec-to-last (second-to-last-measure-candidates-4th
                       position :c previous-melody previous-cantus cantus-note next-cantus)]
      (is (= [[n/c#5 n/d5] [n/c#5 n/b5]] sec-to-last))))

  (testing "candidates 2nd-to-last above"
    (let [position :above
          cantus-key :c
          melody (make-melody n/d5)
          previous-melody n/d5
          previous-cantus n/d4
          m36s nil
          cantus-note n/e4
          cantus-notes [n/f4]
          cand-2nd-to-last (candidates {:position position
                                        :key cantus-key
                                        :melody melody
                                        :m36s m36s;; counter of thirds & sixths
                                        :previous-melody previous-melody
                                        :previous-cantus previous-cantus
                                        :cantus-note cantus-note
                                        :cantus-notes cantus-notes})]
      (is (= [[n/c#5 n/d5] [n/c#5 n/b5]] cand-2nd-to-last))))

  (testing "candidates 2nd-to-last below"
    (let [position :below
          cantus-key :c
          melody (make-melody n/d3)
          previous-melody n/d3
          previous-cantus n/d4
          m36s nil
          cantus-note n/e4
          cantus-notes [n/f4]
          cand-2nd-to-last (candidates {:position position
                                        :key cantus-key
                                        :melody melody
                                        :m36s m36s;; counter of thirds & sixths
                                        :previous-melody previous-melody
                                        :previous-cantus previous-cantus
                                        :cantus-note cantus-note
                                        :cantus-notes cantus-notes})]
      (is (= [[n/c#3 n/d3] [n/c#3 n/a3]] cand-2nd-to-last))))

  (testing "2nd-to-last-below unison"
    (let [position :below
          previous-melody n/g4
          previous-cantus n/g4
          cantus-note n/a5
          next-cantus n/b5
          sec-to-last (second-to-last-measure-candidates-4th
                       position :g previous-melody previous-cantus cantus-note next-cantus)]
      (is (= [[n/f#4 n/g4] [n/f#4 n/d4]] sec-to-last))))

  (testing "candidates last below"
    (let [position :below
          cantus-key :g
          melody []
          previous-melody nil
          previous-cantus nil
          m36s nil
          cantus-note n/g4
          cantus-notes [n/a5 n/b5]
          cand-2nd-to-last (candidates {:position position
                                        :key cantus-key
                                        :melody melody
                                        :m36s m36s;; counter of thirds & sixths
                                        :previous-melody previous-melody
                                        :previous-cantus previous-cantus
                                        :cantus-note cantus-note
                                        :cantus-notes cantus-notes})]
      (is (= (set [[n/g4] [n/g3]]) (set cand-2nd-to-last)))))

  (testing "candidates 3rd-to-last"
    (let [position :above
          cantus-key :c
          melody (make-melody n/d4 n/c#4 n/d4)
          previous-melody n/d4
          previous-cantus n/e3
          cantus-note n/f3
          cantus-notes [n/g3 n/d3]
          m36s {:remaining-cantus-size (inc (count cantus-notes))}
          cand-3rd-to-last (candidates {:position position
                                        :key cantus-key
                                        :melody melody
                                        :m36s m36s
                                        :previous-melody previous-melody
                                        :previous-cantus previous-cantus
                                        :cantus-note cantus-note
                                        :cantus-notes cantus-notes})]
      ;; (println "CAND3" cand-3rd-to-last)
      (is (= (set [[n/d4 n/e4] ;; suspension
                   [n/d4 n/f4]
                   [n/d4 n/a4]
                   [n/d4 n/c4]]) (set cand-3rd-to-last)))))

    (testing "candidates 4th-to-last"
    (let [position :above
          cantus-key :c
          melody (make-melody n/e4 n/d4
                              n/d4 n/c#4 n/d4)
          previous-melody n/e4
          previous-cantus n/f3
          cantus-note n/g3
          cantus-notes [n/a4 n/d3]
          m36s {:remaining-cantus-size (inc (count cantus-notes))}
          cand-4th-to-last (candidates {:position position
                                        :key cantus-key
                                        :melody melody
                                        :m36s m36s
                                        :previous-melody previous-melody
                                        :previous-cantus previous-cantus
                                        :cantus-note cantus-note
                                        :cantus-notes cantus-notes})]
      ;; (println cand-4th-to-last)
      (is (= (set [[n/e4 n/f4] ;; suspension
                   [n/e4 n/b4]
                   [n/e4 n/d4]
                   [n/e4 n/g4]]) (set cand-4th-to-last)))))

  (testing "candidates 5th-to-last"
    (let [position :above
          cantus-key :c
          melody (make-melody n/f4 n/e4
                              n/e4 n/d4
                              n/d4 n/c#4 n/d4)
          previous-melody n/f4
          previous-cantus n/g3
          cantus-note n/a4
          cantus-notes [n/f3 n/d3]
          m36s {:remaining-cantus-size (inc (count cantus-notes))}
          cand-5th-to-last (candidates {:position position
                                        :key cantus-key
                                        :melody melody
                                        :m36s m36s
                                        :previous-melody previous-melody
                                        :previous-cantus previous-cantus
                                        :cantus-note cantus-note
                                        :cantus-notes cantus-notes})]
      ;; (println cand-5th-to-last)
      (is (not (nil? ((set cand-5th-to-last) [n/f4 n/a5]))))
      (is (not (nil? ((set cand-5th-to-last) [n/f4 n/c4]))))
      (is (not (nil? ((set cand-5th-to-last) [n/f4 n/e4]))))

      (is (= 3 (count cand-5th-to-last)))))

  (testing "candidates 6th-to-last"
    (let [position :above
          cantus-key :c
          melody (make-melody n/c4 n/f4
                              n/f4 n/e4
                              n/e4 n/d4
                              n/d4 n/c#4 n/d4)
          previous-melody n/c4
          previous-cantus n/a4
          cantus-note n/f3
          cantus-notes [n/g3 n/d3]
          m36s {:remaining-cantus-size (inc (count cantus-notes))}
          cand-6th-to-last (candidates {:position position
                                        :key cantus-key
                                        :melody melody
                                        :m36s m36s
                                        :previous-melody previous-melody
                                        :previous-cantus previous-cantus
                                        :cantus-note cantus-note
                                        :cantus-notes cantus-notes})]
      ;; (println cand-6th-to-last)
      (is (not (nil? ((set cand-6th-to-last) [n/c4 n/a4]))))
      (is (not (nil? ((set cand-6th-to-last) [n/c4 n/d4]))))
      ;; (is (not (nil? ((set cand-6th-to-last) [n/c4 n/f4]))))

      ;; it is not permissible to proceed from the unison g3-g3 to the second f3-g3
      (is (nil? ((set cand-6th-to-last) [n/f3 n/g3]))))))

(testing "do not allow unprepared (dissonant upbeat) suspension"
  (let [position :above
        cantus-key :c
        melody (make-melody n/bb4 n/g4)
        previous-melody n/g4
        previous-cantus n/g3
        cantus-note n/d3
        cantus-notes [n/e3 n/d3]
        m36s {:remaining-cantus-size (inc (count cantus-notes))}
        cand (candidates {:position position
                          :key cantus-key
                          :melody melody
                          :m36s m36s
                          :previous-melody previous-melody
                          :previous-cantus previous-cantus
                          :cantus-note cantus-note
                          :cantus-notes cantus-notes})]
    ;; (println "CAND susp" cand)
    (is (every? #(not= n/g4 (first %)) cand))))


(deftest candidates-below
  (testing "candidates below"
    (let [position :below
          cantus-key :f
          melody (make-melody n/a3 n/bb3)
          previous-melody n/bb3
          previous-cantus n/f3
          cantus-note n/g3
          cantus-notes [n/d3 n/d3]
          m36s {:remaining-cantus-size (inc (count cantus-notes))}
          cand (candidates {:position position
                            :key cantus-key
                            :melody melody
                            :m36s m36s
                            :previous-melody previous-melody
                            :previous-cantus previous-cantus
                            :cantus-note cantus-note
                            :cantus-notes cantus-notes})]
      ;; (println cand)
      (is (not (nil? ((set cand) [n/bb3 n/c3]))))
      (is (not (nil? ((set cand) [n/bb3 n/g2]))))))

  (testing "candidates no dim5 suspension"
    (let [position :below
          cantus-key :c
          melody (make-melody n/g2 n/a3)
          previous-melody n/a3
          previous-cantus n/d3
          cantus-note n/e3
          cantus-notes [n/f3 n/c3]
          m36s {:remaining-cantus-size (inc (count cantus-notes))}
          cand (candidates {:position position
                            :key cantus-key
                            :melody melody
                            :m36s m36s
                            :previous-melody previous-melody
                            :previous-cantus previous-cantus
                            :cantus-note cantus-note
                            :cantus-notes cantus-notes})]
      (println cand)
      (is (not (contains? (set cand) [n/a3 n/b3]))))))

(deftest candidates-leap-recovery
  ;; check fux-f 100
  (testing "candidates for leap recovery"
    (let [position :above
          cantus-key :c
          melody (make-melody n/c5 n/a5)
          previous-melody n/a5
          previous-cantus n/a4
          cantus-note n/c4
          cantus-notes [n/f3 n/e3]
          m36s {:remaining-cantus-size (inc (count cantus-notes))}
          cand (candidates {:position position
                            :key cantus-key
                            :melody melody
                            :m36s m36s
                            :previous-melody previous-melody
                            :previous-cantus previous-cantus
                            :cantus-note cantus-note
                            :cantus-notes cantus-notes})]
      ;; (println "CAND" cand)
      ;; a5 is a suspension; e4 to a5 is a leap of 4
      ;; which is not recovered in next measure
      (is (nil? ((set cand) [n/a5 n/e4]))))))

(deftest generate-fourth-test
  (testing "above"
    (let [fux-d-cf (get-melody fux-d)
          cps (generate-reverse-counterpoint-dfs :above :f fux-d-cf candidates)
          cp (dfs-solution->cp (first cps))
          species (make-fourth-species fux-d-cf cp :above)]
      ;; (println species)
      (is (evaluate-fourth-species species))
      (is (fourth-species-rules? species))))

  (testing "below"
    (let [fux-d-cf (get-melody fux-d)
          cps (generate-reverse-counterpoint-dfs :below :f fux-d-cf candidates)
          cp (dfs-solution->cp (first cps))
          species (make-fourth-species fux-d-cf cp :below)]
      ;; (println species)
      (is (evaluate-fourth-species species))
      (is (fourth-species-rules? species)))))

(deftest generate-fourth-all-eval
  (testing "consecutive suspensions of same kind"
    (let [test-cf
          (make-melody n/d3 n/a4 n/g3 n/f3 n/e3 n/d3)
          cps (generate-reverse-counterpoint-dfs :above :c test-cf
                                                 candidates
                                                 next-node-4th)
          sp-coll (mapv #(make-fourth-species test-cf
                                              (dfs-solution->cp %)
                                              :above)
                        cps)
          scores (map #(evaluate-fourth-species %) sp-coll)
          markup-fn (fn [idx] (str "Ex. " idx " score: " (nth scores idx)))]
      (println "CPS" (count cps))
      (species-coll->lily sp-coll
                          {:markup-fn markup-fn
                           :file "all"
                           :folder "all"}))))
