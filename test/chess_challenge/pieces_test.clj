(ns chess-challenge.pieces-test
  (:require [clojure.test :refer :all]
            [chess-challenge.pieces :refer :all]))

(deftest validation-test
  (testing "pieces validation"
    (is (thrown? Exception (pieces-validation '()))
        "empty list")
    (is (thrown? Exception (pieces-validation '(:queen :rook :no-piece :king)))
        "not exist piece")
    (is (nil? (pieces-validation '(:queen :rook :bishop :king :knight)))
        "pass pieces")))

(deftest capture-test
  (testing "capture square by piece"
    (is (false? (capture? (first {[2 3] :queen}) [1 1])) "capture by queen")
    (is (true? (capture? (first {[2 3] :queen}) [1 2])) "capture by queen")
    (is (true? (capture? (first {[2 3] :queen}) [4 3])) "capture by queen")
    (is (false? (capture? (first {[2 3] :rook}) [3 4])) "capture by rook")
    (is (true? (capture? (first {[2 3] :rook}) [4 3])) "capture by rook")
    (is (false? (capture? (first {[2 3] :bishop}) [2 4])) "capture by bishop")
    (is (true? (capture? (first {[2 3] :bishop}) [3 4])) "capture by bishop")
    (is (false? (capture? (first {[2 3] :king}) [4 1])) "capture by king")
    (is (true? (capture? (first {[2 3] :king}) [3 4])) "capture by king")
    (is (true? (capture? (first {[2 3] :king}) [2 4])) "capture by king")
    (is (true? (capture? (first {[2 3] :king}) [2 4])) "capture by king")
    (is (false? (capture? (first {[2 3] :knight}) [2 2])) "capture by knight")
    (is (true? (capture? (first {[2 3] :knight}) [1 1])) "capture by knight")
    (is (true? (capture? (first {[2 3] :knight}) [4 2])) "capture by knight")))

(deftest capture-themself-test
  (testing "capture piece by themself?"
    (is (true? (capture? (first {[2 3] :queen}) [2 3])) "capture by queen")
    (is (true? (capture? (first {[2 3] :rook}) [2 3])) "capture by rook")
    (is (true? (capture? (first {[2 3] :bishop}) [2 3])) "capture by bishop")
    (is (true? (capture? (first {[2 3] :king}) [2 3])) "capture by king")
    (is (true? (capture? (first {[2 3] :knight}) [2 3])) "capture by knight")))

(deftest piece-capture-any-test
  (testing "capture any of squares by piece"
    (is (false? (capture-any? (first {[2 3] :queen}) [[1 1] [3 1] [4 2] [4 4]])) "queen capture any square?")
    (is (true? (capture-any? (first {[2 3] :queen}) [[1 1] [3 1] [4 2] [3 4] [4 4]])) "queen capture any square?")))

(deftest remove-capture-squares-test
  (testing "remove capture squares by piece"
    (is (= [[1 1] [3 1] [4 2] [4 4]] (remove-capture-squares (first {[2 3] :queen}) [[1 1] [3 1] [4 2] [3 4] [4 4]])) "remove squares capture by queen")
    (is (= [[3 4]] (remove-capture-squares (first {[2 3] :knight}) [[1 1] [3 1] [4 2] [3 4] [4 4]])) "remove squares capture by knight")))