(ns counterpoint.generate-second-species-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.generate-second-species :refer [generate-reverse-random-counterpoint-second]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.second-species :refer [make-second-species
                                                 second-species-rules?]]))

(deftest generate-second-short-test
  (testing "above"
    (let [cf (make-melody n/d4 n/e4 n/d4)
          cp (generate-reverse-random-counterpoint-second :above :c cf)
          species (make-second-species cf cp :above)
          ]
      ;; (println species)
      (is 
       (second-species-rules? species)
          )))

  (testing "below"
    (let [cf (make-melody n/d4 n/e4 n/d4)
          cp (generate-reverse-random-counterpoint-second :below :c cf)
          species (make-second-species cf cp :below)]
      ;; (println species)
      (is (second-species-rules? species))))
  )

(deftest generate-second-test
  ;; (testing "above"
  ;;   (let [cf (make-melody n/d4 n/f4 n/e4 n/d4)
  ;;         cp (generate-reverse-random-counterpoint-second :above :c cf)
  ;;         species (make-second-species cf cp :above)]
  ;;     (println species)
  ;;     (is
  ;;      (second-species-rules? species))))

  ;; (testing "below"
  ;;   (let [cf (make-melody n/d4 n/f4 n/e4 n/d4)
  ;;         cp (generate-reverse-random-counterpoint-second :below :c cf)
  ;;         species (make-second-species cf cp :below)]
  ;;     (println species)
  ;;     (is (second-species-rules? species))))
  )