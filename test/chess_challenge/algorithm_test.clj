(ns chess-challenge.algorithm-test
  (:require [clojure.test :refer :all]
            [chess-challenge.algorithm :refer :all]))

(defn random-pieces [n]
  (take n (repeatedly #(rand-nth '(:queen :rook :bishop :king :knight)))))

(defn reflect-board [[cols rows] board]
  (let [squares (keys board) pieces (vals board)]
    (hash-set
      ; vertical
      (-> (fn [[col row]] [col (inc (- rows row))])
          (map squares)
          (zipmap pieces))
      ; horizontal
      (-> (fn [[col row]] [(inc (- cols col)) row])
          (map squares)
          (zipmap pieces))
      ; vertical and horizontal
      (-> (fn [[col row]] [(inc (- cols col)) (inc (- rows row))])
          (map squares)
          (zipmap pieces)))))

(defn rotate-90-board [[cols _] board]
  (-> (fn [[col row]] [(inc (- cols row)) col])
      (map (keys board))
      (zipmap (vals board))))

(defn add-reflect-board [board-size boards board]
  (reduce conj boards (reflect-board board-size board)))

(defn add-rotate-90-board [[cols rows] boards board]
  (conj boards (rotate-90-board [cols rows] board)))

(defn add-reflect-boards [board-size boards]
  (-> (partial add-reflect-board board-size)
      (reduce boards boards)))

(defn add-rotate-90-boards [[cols rows] boards]
  (if (= cols rows)
    (-> (partial add-rotate-90-board [cols rows])
        (reduce boards boards))
    boards))

(defn duplicate-boards [board-size boards]
  "Generate all possible duplicate boards by reflect and rotate board."
  (add-rotate-90-boards board-size (add-reflect-boards board-size boards)))

(defn correct-solutions? [board-size boards]
  "Check completeness of solutions based on reflections and rotation propeties of board."
  (= boards (duplicate-boards board-size boards)))





(deftest sort-pieces-test
  (testing "sort pieces by heurystic method"
    (is (= '(:queen :rook :bishop :king :knight)
           (sort-pieces '(:knight :king :bishop :rook :queen))))))

(deftest empty-boards-pool-test
  (testing "metadata in empty-board-pool"
    (is (= [3 4]
           (:board-size (meta (empty-boards-pool [3 4]))))
        "board-size metadata")
    (is (= '([1 1] [1 2] [2 1] [2 2])
           (:safe-squares (meta (first (empty-boards-pool [2 2])))))
        "safe-squares metadata")))

(deftest add-piece-to-board-test
  (testing "add piece to board"
    (is (= {[2 3] :queen}
           (add-piece-to-board {} (first {[2 3] :queen})))
        "add piece to empty board")
    (is (= {[2 3] :queen [1 1] :king [3 4] :bishop}
           (add-piece-to-board {[1 1] :king [3 4] :bishop} (first {[2 3] :queen})))
        "add piece to board with pieces")
    (is (= '([1 3] [1 4])
           (-> (with-meta {[3 3] :king} {:safe-squares '([1 1] [2 1] [3 1] [4 1] [1 2] [1 3] [1 4])})
               (add-piece-to-board (first {[2 1] :queen}))
               (meta)
               (:safe-squares)))
        "safe-quares metadata update after add piece")))

(deftest algorithm-test
  (testing "algorithm"
    (is (= #{{[1 1] :queen}} (solutions [1 1] '(:queen)))
        "1x1 1queen")
    (is (= #{{[1 1] :king [3 1] :king [2 3] :rook}
             {[1 1] :king [1 3] :king [3 2] :rook}
             {[3 1] :king [3 3] :king [1 2] :rook}
             {[1 3] :king [3 3] :king [2 1] :rook}}
           (solutions [3 3] '(:rook :king :king)))
        "3x3 2kings 1rook")
    (is (= #{{[2 1] :knight [4 1] :knight [2 3] :knight [4 3] :knight [3 2] :rook [1 4] :rook}
             {[2 1] :knight [4 1] :knight [2 3] :knight [4 3] :knight [1 2] :rook [3 4] :rook}
             {[2 2] :knight [4 2] :knight [2 4] :knight [4 4] :knight [1 1] :rook [3 3] :rook}
             {[2 2] :knight [4 2] :knight [2 4] :knight [4 4] :knight [3 1] :rook [1 3] :rook}
             {[1 2] :knight [3 2] :knight [1 4] :knight [3 4] :knight [2 1] :rook [4 3] :rook}
             {[1 2] :knight [3 2] :knight [1 4] :knight [3 4] :knight [4 1] :rook [2 3] :rook}
             {[1 1] :knight [3 1] :knight [1 3] :knight [3 3] :knight [4 2] :rook [2 4] :rook}
             {[1 1] :knight [3 1] :knight [1 3] :knight [3 3] :knight [2 2] :rook [4 4] :rook}}
           (solutions [4 4] '(:rook :rook :knight :knight :knight :knight)))
        "4x4 2rooks 4knights")
    (is (= (solutions [8 8] '(:queen :queen :queen :queen :queen :queen :queen :queen))
           ;; create all 92 solutions from 12 unique wrote by hand for 8x8 8queens
           (duplicate-boards [8 8]
                             #{{[4 1] :queen [7 2] :queen [3 3] :queen [8 4] :queen [2 5] :queen [5 6] :queen [1 7] :queen [6 8] :queen}
                               {[5 1] :queen [2 2] :queen [4 3] :queen [7 4] :queen [3 5] :queen [8 6] :queen [6 7] :queen [1 8] :queen}
                               {[4 1] :queen [2 2] :queen [7 3] :queen [3 4] :queen [6 5] :queen [8 6] :queen [5 7] :queen [1 8] :queen}
                               {[4 1] :queen [6 2] :queen [8 3] :queen [3 4] :queen [1 5] :queen [7 6] :queen [5 7] :queen [2 8] :queen}
                               {[3 1] :queen [6 2] :queen [8 3] :queen [1 4] :queen [4 5] :queen [7 6] :queen [5 7] :queen [2 8] :queen}
                               {[5 1] :queen [3 2] :queen [8 3] :queen [4 4] :queen [7 5] :queen [1 6] :queen [6 7] :queen [2 8] :queen}
                               {[5 1] :queen [7 2] :queen [4 3] :queen [1 4] :queen [3 5] :queen [8 6] :queen [6 7] :queen [2 8] :queen}
                               {[4 1] :queen [1 2] :queen [5 3] :queen [8 4] :queen [6 5] :queen [3 6] :queen [7 7] :queen [2 8] :queen}
                               {[3 1] :queen [6 2] :queen [4 3] :queen [1 4] :queen [8 5] :queen [5 6] :queen [7 7] :queen [2 8] :queen}
                               {[6 1] :queen [2 2] :queen [7 3] :queen [1 4] :queen [4 5] :queen [8 6] :queen [5 7] :queen [3 8] :queen}
                               {[4 1] :queen [7 2] :queen [1 3] :queen [8 4] :queen [5 5] :queen [2 6] :queen [6 7] :queen [3 8] :queen}
                               {[6 1] :queen [4 2] :queen [7 3] :queen [1 4] :queen [8 5] :queen [2 6] :queen [5 7] :queen [3 8] :queen}}))
        "8x8 8queens")))

(deftest random-algorithm-test
  (let [pieces (random-pieces (inc (rand-int 4)))]
    (testing "random examples"
      (let [size (* 2 (inc (rand-int 4)))]
        (is (true? (correct-solutions? [size size] (solutions [size size] pieces)))
            (str "even square board " size "x" size " pieces: " (pr-str pieces))))

      (let [size (inc (* 2 (rand-int 4)))]
        (is (true? (correct-solutions? [size size] (solutions [size size] pieces)))
            (str "odd square board " size "x" size " pieces: " (pr-str pieces))))

      (let [cols (inc (rand-int 8)) rows (inc cols)]
        (is (true? (correct-solutions? [cols rows] (solutions [cols rows] pieces)))
            (str "not square board" cols "x" rows " pieces: " (pr-str pieces)))))))