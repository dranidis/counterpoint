(ns counterpoint.generate-first-species-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.first-species :refer [first-species-rules?]]
            [counterpoint.first-species-type :refer [make-first-species]]
            [counterpoint.generate-first-species :refer [generate-reverse-random-counterpoint]]))

;; (deftest generate-first-test
;;   (testing "above"
;;     (let [cp (generate-reverse-random-counterpoint :above :c fux-d)
;;           species (make-first-species fux-d cp :above)]
;;       (is (first-species-rules? species))))
  
;;   (testing "below"
;;     (let [cp (generate-reverse-random-counterpoint :below :c fux-d)
;;           species (make-first-species fux-d cp :below)]
;;       (is (first-species-rules? species)))))