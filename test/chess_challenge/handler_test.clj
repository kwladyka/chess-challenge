(ns chess-challenge.handler-test
  (:require [clojure.test :refer :all]
            [chess-challenge.handler :refer [get-solutions]]))

(deftest get-solutions-test
  (testing "get-solutions")
  (is (= #{{[1 1] :king [3 1] :king [2 3] :rook}
           {[1 1] :king [1 3] :king [3 2] :rook}
           {[3 1] :king [3 3] :king [1 2] :rook}
           {[1 3] :king [3 3] :king [2 1] :rook}}
         (get-solutions [3 3] '(:rook :king :king)))))