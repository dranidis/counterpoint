(ns counterpoint.utils-test 
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.notes :as n]
            [counterpoint.utils :refer [get-4th-species-from-bar-melody]]))



(deftest get-4th-species-from-bar-melody-test
  (testing "stripping the durations and keeping only the notes"
    (let [bar-melody [
                      [{:d 2 :n [:rest n/c4]}]
                      [{:d 2 :n [n/e4 n/f4]}]
                      [{:d 1 :n [n/g4]}]]
          actual (get-4th-species-from-bar-melody bar-melody)
          expected [:rest n/c4 n/e4 n/f4 n/g4]]
      (is (= expected actual)))))