(ns counterpoint.intervals-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.intervals :refer [note-at-diatonic-interval m3 m3-]]
            [counterpoint.notes :as n]))

(deftest get-note-at-interval-test
  (testing "key c: a 3"
    (is (= (n/make-note-nooctave :c :natural) (note-at-diatonic-interval :c :a 3))))

  (testing "key c: a -3"
    (is (= (n/make-note-nooctave :f :natural) (note-at-diatonic-interval :c :a -3))))

  (testing "key d: f# 5"
    (is (= (n/make-note-nooctave :c :sharp) (note-at-diatonic-interval :d :f 5))))

  (testing "key db: a 1 3 5 6"
    (is (= [(n/make-note-nooctave :a :flat)
            (n/make-note-nooctave :c :natural)
            (n/make-note-nooctave :e :flat)
            (n/make-note-nooctave :f :natural)]
           (map #(note-at-diatonic-interval :db :a %) [1 3 5 6])))))