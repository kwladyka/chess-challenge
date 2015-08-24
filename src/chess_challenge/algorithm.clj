(ns chess-challenge.algorithm
  (:require [chess-challenge.board :refer [all-board-squares]]
            [chess-challenge.pieces :refer [capture-any? remove-capture-squares]]))

(defn sort-pieces [pieces]
  "Sort pieces by heurystic method."
  (sort-by {:queen 1 :rook 2 :bishop 3 :king 4 :knight 5} pieces))

(defn empty-boards-pool [board-size]
  (-> #{(with-meta {} {:safe-squares (all-board-squares board-size)})}
      (with-meta {:board-size board-size})))

(defn add-piece-to-board [board piece]
  (-> (conj board piece)
      (vary-meta update :safe-squares (partial remove-capture-squares piece))))

(defn add-board-if-pass [piece board boards-pool safe-square]
  (let [piece-notation (first {safe-square piece})]
    (if (capture-any? piece-notation (keys board))
      boards-pool
      (conj boards-pool (add-piece-to-board board piece-notation)))))

(defn next-stages-of-board [piece created-boards board]
  "Create from one board many new boards by add new piece in all possible safe squares.
  Only boards that piecies don't capture any others pass."
  (-> (partial add-board-if-pass piece board)
      (reduce created-boards (:safe-squares (meta board)))))

(defn next-stages-of-boards [boards-pool piece]
  (-> (partial next-stages-of-board piece)
      (reduce (empty boards-pool) boards-pool)))

(defn solutions [board-size pieces]
  (-> (partial next-stages-of-boards)
      (reduce (empty-boards-pool board-size)(sort-pieces pieces))))