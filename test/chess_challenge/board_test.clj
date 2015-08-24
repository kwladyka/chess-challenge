(ns chess-challenge.board-test
  (:require [clojure.test :refer :all]
            [chess-challenge.board :refer [board-validation]]))

(deftest validation-test
  (testing "create variations of the board"
    (is (thrown? Exception (board-validation [0 10]))
        "non positive dimension")
    (is (thrown? Exception (board-validation [10 -10]))
        "negative dimension")
    (is (thrown? Exception (board-validation [10 3.45]))
        "not ingeter")
    (is (nil? (board-validation [10 10]))
        "pass values")))