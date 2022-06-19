(ns counterpoint.key-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.key :refer [get-note-acc-at-key]]))

(deftest get-note-acc-at-key-test
  (testing "c"
    (is (= :natural (get-note-acc-at-key :c :f))))
  
  (testing "g"
    (is (= :sharp (get-note-acc-at-key :g :f))))
  
  (testing "cb"
    (is (= :flat (get-note-acc-at-key :cb :f))))

  )