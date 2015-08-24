(ns chess-challenge.board)

(defn board-validation [[cols rows]]
  (when (not ((every-pred pos? integer?) cols rows))
    (throw (Exception. "Size dimensions of the board has to be in positive integer."))))

(defn all-board-squares [[cols rows]]
  (for [col (range 1 (inc cols))
        row (range 1 (inc rows))]
    [col row]))