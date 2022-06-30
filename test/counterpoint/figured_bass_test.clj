(ns counterpoint.figured-bass-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus-firmi-examples :refer [fux-d salieri-c]]
            [counterpoint.figured-bass :refer [figured-bass-first
                                               figured-bass-fourth figured-bass-second]]
            [counterpoint.first-species-type :refer [make-first-species]]
            [counterpoint.fourth-species :refer [make-fourth-species]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.rest :as rest]
            [counterpoint.second-species :refer [make-second-species]]))

(deftest figured-bass

  (testing "first"
    (let [shubert-first-species-below-salieri-c
          (make-first-species salieri-c
                              (make-melody
                               n/c3 n/d3 n/c3 n/c3 n/e3 n/a3 n/a3 n/b3 n/c3)
                              :below)]
      (is (= "<10>1<6>1<6>1<3>1<3>1<6>1<3>1<6>1<8>1"
             (figured-bass-first shubert-first-species-below-salieri-c)))))
  
  (testing "second"
    (let [salzer-fux-d (let [counterpoint-melody
                             (make-melody rest/r
                          ;;  n/a4 ;; first a4 should be a rest when implemented!!
                                          n/d4
                                          n/a4 n/b4
                                          n/c4 n/g3
                                          n/f3 n/a4
                                          n/bb4 n/g3
                                          n/a4 n/f4
                                          n/e4 n/f4
                                          n/g4 n/e4
                                          n/a5 n/a4
                                          n/b4 n/c#4
                                          n/d4)]
                         (make-second-species fux-d counterpoint-melody :above))]
      (is (= "<_>2<8>2<3>2<4>2<6>2<3>2<3>2<5>2<3>2<1>2<3>2<8>2<5>2<6>2<8>2<6>2<10>2<3>2<5>2<6>2<8>2"
             (figured-bass-second salzer-fux-d)))))

  (testing "fourth"
    (let [fux-d-4-species (make-fourth-species
                           fux-d
                           (make-melody n/a4 n/a4
                                        n/d4 n/d4 n/c4 n/c4 n/bb4 n/bb4
                                        n/g3
                                        n/a4
                                        n/c4 n/c4
                                        n/f4 n/f4 n/e4 n/e4 n/d4 n/d4 n/c#4
                                        n/d4)
                           :above)]
      (is (= "<_>2<5>2<3>2<6>2<7>2<6>2<7>2<6>2<3>2<1>2<3>2<5>2<3>2<6>2<7>2<6>2<7>2<6>2<7>2<6>2<8>2"
             (figured-bass-fourth fux-d-4-species))))))