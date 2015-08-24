(ns chess-challenge.handler
  (:gen-class)
  (:require [chess-challenge.board :refer [board-validation]]
            [chess-challenge.pieces :refer [pieces-validation]]
            [chess-challenge.algorithm :refer [solutions]]
            [clojure.pprint :refer [pprint]]))

(defn get-solutions [[cols rows] pieces]
  (board-validation [cols rows])
  (pieces-validation pieces)
  (solutions [cols rows] pieces))

(defn -main []
  (let [board-size [7 7] pieces '(:king :king :queen :queen :bishop :bishop :knight)]
    (println "Solutions for board size" board-size "and pieces" pieces)
    (println "Please wait...")
    (let [boards (time (solutions board-size pieces))]
      (println "Found solutions:" (count boards))
      (println "First 10 solutions:")
      (pprint (take 10 boards)))))