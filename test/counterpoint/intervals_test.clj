(ns counterpoint.intervals-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.intervals :refer [A1 A2 A3 diatonic m10 m13 m14 M14
                                            m2 M2 m2- M2- m3 M3 m3- M3- m6 M6
                                            m6- M6- m7 M7 m7- M7- m9 M9 make-interval nooctave-note-at-diatonic-interval
                                            note->number-of-semitones note-at-melodic-interval P1 P11 P12 P4 P4- P5 P5- P8 P8-]]
            [counterpoint.key :refer [make-key]]
            [counterpoint.notes :as n :refer [num2->note]]))


(deftest note->number-of-semitones-test
  (testing "a0"
    (is (= 0 (note->number-of-semitones (n/make-note :a 0 :natural)))))

  (testing "a1"
    (is (= 12 (note->number-of-semitones (n/make-note :a 1 :natural)))))

  (testing "bb1"
    (is (= 13 (note->number-of-semitones (n/make-note :b 1 :flat)))))

  (testing "g#2"
    (is (= 35 (note->number-of-semitones (n/make-note :g 2 :sharp))))))

(deftest get-note-at-interval-test
  (testing "key c: a 3"
    (is (= (n/make-note-nooctave :c :natural)
           (nooctave-note-at-diatonic-interval (make-key :c) (n/make-note-nooctave :a :natural) 3))))

  (testing "key c: a -3"
    (is (= (n/make-note-nooctave :f :natural)
           (nooctave-note-at-diatonic-interval (make-key :c) (n/make-note-nooctave :a :natural) -3))))

  (testing "key d: f# 5"
    (is (= (n/make-note-nooctave :c :sharp)
           (nooctave-note-at-diatonic-interval (make-key :d) (n/make-note-nooctave :f :natural) 5))))

  (testing "key db: a 1 3 5 6"
    (is (= [(n/make-note-nooctave :a :flat)
            (n/make-note-nooctave :c :natural)
            (n/make-note-nooctave :e :flat)
            (n/make-note-nooctave :f :natural)]
           (map #(nooctave-note-at-diatonic-interval (make-key :db) (n/make-note-nooctave :a :natural) %) [1 3 5 6])))))

(deftest get-note-at-melodic-interval-test
  (testing "key c: a m3"
    (is (= n/c4
           (note-at-melodic-interval n/a4 m3))))

  (testing "key c: a M3"
    (is (= n/c#4
           (note-at-melodic-interval n/a4 M3))))

  (testing "key c: a m2-"
    (is (= n/g#3
           (note-at-melodic-interval n/a4 m2-))))

  (testing "key c: a P8-"
    (is (= n/a3
           (note-at-melodic-interval n/a4 P8-))))

  (testing "all intervals neg from a"
    (is (= [[:g 3 :sharp]
            [:g 3 :natural]
            [:f 3 :sharp]
            [:f 3 :natural]
            [:e 3 :natural]
            [:d 3 :natural]
            [:c 3 :sharp]
            [:c 3 :natural]
            [:b 3 :natural]
            [:b 3 :flat]
            [:a 3 :natural]
            [:g 2 :sharp]]
           (map
            (fn [i] (note-at-melodic-interval n/a4 i))
            [m2- M2- m3- M3- P4- P5- m6- M6- m7- M7- P8- (make-interval -9 :minor)]))))

  (testing "positive intervals from f#"
    (is (= [[:f 4 :sharp] [:g 4 :natural] [:g 4 :sharp] [:a 5 :natural] [:a 5 :sharp]
            [:b 5 :natural] [:c 5 :sharp] [:d 5 :natural] [:d 5 :sharp]
            [:e 5 :natural] [:e 5 :sharp] [:f 5 :sharp]
            [:g 5 :natural] [:g 5 :sharp] [:a 6 :natural] [:b 6 :natural]
            [:c 6 :sharp] [:d 6 :natural] [:e 6 :natural] [:e 6 :sharp]]
           (map
            (fn [i] (note-at-melodic-interval n/f#4 i))
            [P1 m2 M2 m3 M3 P4 P5 m6 M6 m7 M7 P8 m9 M9 m10 P11 P12 m13 m14 M14])))))

(testing "aug f#"
  (is (= [[:f 4 :double-sharp] [:g 4 :double-sharp] [:a 5 :double-sharp]]
         (map
          (fn [i] (note-at-melodic-interval n/f#4 i))
          [A1 A2 A3]))))

(deftest some-test
      (testing "Context of the test assertions"
        (is (= n/a5 (note-at-melodic-interval n/c4 M6))))) 

(deftest diatonic-test
  (testing "testing diatonic intervals from c"
    (let [
          actual (mapv #(diatonic :c n/c4 %)
                       (range 1 11))
          expected [n/c4 n/d4 n/e4 n/f4 n/g4 n/a5 n/b5 n/c5 n/d5 n/e5]
          ]
      (is (= n/a5 (diatonic :c n/c4 6)))
      (is (= n/b5 (diatonic :c n/c4 7)))
      
      ;; (map #(is %1 %2) expected actual)
      (is (= expected actual))
      ))
  
  (testing "testing diatonic intervals from a"
    (let [actual (mapv #(diatonic :c n/a4 %)
                       (range 1 11))
          expected [n/a4 n/b4 n/c4 n/d4 n/e4 n/f4 n/g4 n/a5 n/b5 n/c5]]
      
      (is (= n/b5 (diatonic :c n/a5 2)))
      ;; (map #(is %1 %2) expected actual)
      (is (= expected actual))))
  
  (testing "testing diatonic intervals from a"
    (let [
          ;; actual (mapv #(diatonic :c n/a4 %)
          ;;              (range 1 11))
          ;; expected [n/a4 n/b4 n/c4 n/d4 n/e4 n/f4 n/g4 n/a5 n/b5 n/c5]
          ]

      (is (= n/e4 (diatonic :c n/a5 -4)))
      ;; (map #(is %1 %2) expected actual)
      ;; (is (= expected actual))
          )))



