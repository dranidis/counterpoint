(ns counterpoint.gen-first-dfs-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [get-melody]]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.first-species :refer [first-species-rules?]]
            [counterpoint.first-species-type :refer [make-first-species]]
            [counterpoint.gen-first-dfs :refer [dfs-solution->cp
                                                generate-reverse-counterpoint-dfs]]))

(deftest generate-first-test
  (testing "above"
    (let [fux-d-cf (get-melody fux-d)
          cps (generate-reverse-counterpoint-dfs :above :c fux-d-cf)
          cp (dfs-solution->cp (first cps))
          species (make-first-species fux-d-cf cp :above)]
      (is (first-species-rules? species))))

  (testing "below"
    (let [fux-d-cf (get-melody fux-d)
          cps (generate-reverse-counterpoint-dfs :below :c fux-d-cf)
          cp (dfs-solution->cp (first cps))
          species (make-first-species fux-d-cf cp :below)]
      (is (first-species-rules? species)))))