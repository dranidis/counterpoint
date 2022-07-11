(ns counterpoint.second-species-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.second-species :refer [downbeats-second
                                                 make-second-species
                                                 no-undisguised-direct-motion-of-downbeats-to-perfect? passing-tone
                                                 undisguised-direct-motion-of-downbeats-to-perfect]]))

(deftest downbeats-test
  (testing "one note melody"
    (let [counterpoint-melody
          (make-melody n/c4)]
      (is (= (make-melody n/c4)
             (downbeats-second counterpoint-melody)))))

  (testing "melody"
    (let [counterpoint-melody
          (make-melody n/g4 n/b4
                       n/a4 n/b4
                       n/c4)]
      (is (= (make-melody n/g4
                          n/a4
                          n/c4)
             (downbeats-second counterpoint-melody))))))

(deftest passing-tone-test
  (testing "g f e"
    (is (passing-tone [n/g4 n/f4 n/e4])))
  (testing "f g a"
    (is (passing-tone [n/f4 n/g4 n/a5])))
  (testing "f g a'"
    (is (not (passing-tone [n/f4 n/g4 n/a4])))))

(deftest undisguised-direct-motion-of-downbeats-to-perfect-test
  (testing "g f e"
    (is (not (undisguised-direct-motion-of-downbeats-to-perfect
              [[:d 3 :natural] [:d 3 :natural] [:e 3 :natural]]
              [[:b 2 :natural] [:b 3 :natural] [:a 3 :natural]])))))

(deftest no-undisguised-direct-motion-of-downbeats-to-perfect?-test
  (testing "undisguised"
    (let [test-sp (let [counterpoint-melody
                        (make-melody n/d3 n/c3
                                     n/bb3 n/d3
                                     n/a3 n/c#3
                                     n/d3)
                        cf (make-melody n/d3
                                        n/f3
                                        n/e3
                                        n/d3)]
                    (make-second-species cf counterpoint-melody :below))]
      (is (not (no-undisguised-direct-motion-of-downbeats-to-perfect? test-sp)))))
  
  (testing "disguised"
    (let [test-sp (let [counterpoint-melody
                        (make-melody n/d3 n/c3
                                     n/bb3 n/f2
                                     n/a3 n/c#3
                                     n/d3)
                        cf (make-melody n/d3
                                        n/f3
                                        n/e3
                                        n/d3)]
                    (make-second-species cf counterpoint-melody :below))]
      (is (no-undisguised-direct-motion-of-downbeats-to-perfect? test-sp)))))

