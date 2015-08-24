(ns chess-challenge.pieces)

(defn pieces-validation [pieces]
  (when (empty? pieces)
    (throw (Exception. "Number of pieces has to be greater then 0.")))
  (when-not (every? #{:queen :rook :bishop :king :knight} pieces)
    (throw (Exception. "You can use only pieces: :queen :rook :bishop :king :knight."))))

(defn rook-capture-square? [[rook-col rook-row] [col row]]
  (or (= col rook-col)
      (= row rook-row)))

(defn bishop-capture-square? [[^long bishop-col ^long bishop-row] [^long col ^long row]]
  (= (Math/abs (- col bishop-col))
     (Math/abs (- row bishop-row))))

(defn queen-capture-square? [queen-square square]
  (or
    (rook-capture-square? queen-square square)
    (bishop-capture-square? queen-square square)))

(defn king-capture-square? [[^long king-col ^long king-row] [^long col ^long row]]
  (and
    (<= (Math/abs (- col king-col)) 1)
    (<= (Math/abs (- row king-row)) 1)))

(defn knight-capture-square? [[^long knight-col ^long knight-row] [^long col ^long row]]
  (let [col-dif (Math/abs (- col knight-col))
        row-dif (Math/abs (- row knight-row))]
    (or
      (and (= col knight-col) (= row knight-row))
      (and (= 1 col-dif) (= 2 row-dif))
      (and (= 2 col-dif) (= 1 row-dif)))))

(defn capture? [piece-notation square]
  (->> (case (val piece-notation)
         :queen (queen-capture-square? (key piece-notation) square)
         :rook (rook-capture-square? (key piece-notation) square)
         :bishop (bishop-capture-square? (key piece-notation) square)
         :king (king-capture-square? (key piece-notation) square)
         :knight (knight-capture-square? (key piece-notation) square))))

(defn capture-any? [piece-notation squares]
  (-> (partial capture? piece-notation)
      (some squares)
      (true?)))

(defn remove-capture-squares [piece-notation squares]
  (-> (partial capture? piece-notation)
      (remove squares)))