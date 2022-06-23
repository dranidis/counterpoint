(ns counterpoint.second-species-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.second-species :refer [downbeats-second]]))

(deftest downbeats-test
  (testing "melody"
    (let [counterpoint-melody
          (make-melody n/c4 n/b4
                       n/a4 n/b4
                       n/c4 n/g4
                       n/e4 n/c4
                       n/b4 n/d4
                       n/a4 n/b4
                       n/c4 n/g3
                       n/a4 n/b4
                       n/c4)]
      (is (= (make-melody n/c4
                          n/a4
                          n/c4
                          n/e4
                          n/b4
                          n/a4
                          n/c4
                          n/a4
                          n/c4)
             (downbeats-second counterpoint-melody))))))