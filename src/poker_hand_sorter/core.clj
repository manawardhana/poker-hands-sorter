(ns poker-hand-sorter.core
  (:require [clojure.string :as str])
  (:gen-class))

(def ranks [
  :high-card      ;Highest value card
  :pair            ;Two cards of same value
  :two-pairs       ;Two different pairs
  :three-of-a-kind ;Three cards of the same value
  :straight        ;All five cards in consecutive value order
  :flush           ;All five cards having the same suit
  :full-house      ;Three of a kind and a Pair
  :four-of-a-kind  ;Four cards of the same value
  :straight-flush  ;All five cards in consecutive value order, with the same suit
  :royal-flush     ;Ten, Jack, Queen, King and Ace in the same suit
])

(def cards
   [\2, \3, \4, \5, \6, \7, \8, \9, \T, \J, \Q, \K, \A])

(defn get-value [s]
  (.indexOf cards (first s)))

(defn cint [c]
  "Converts Character digit into an int"
  (read-string (str c)))


;-------- SORT FUNCTIONS ---------

(defn high-card-value [a b]
  (let [a-vals (map get-value (sort-by get-value a))
        b-vals (map get-value (sort-by get-value b))
        zipped-vals (filter #(not= (first %) (second %)) (map list a-vals b-vals))
        last-pair (last zipped-vals)]
     (if (> (first last-pair) (second last-pair)) :h1 :h2))) 
      

(defn pair? [c]
  (let [b (map get-value c)]
    (= 2 (apply max (vals (frequencies b))))))

(defn two-pairs? [c] 
  (let [b (map get-value c)]
  (and (= 3 (count (frequencies b))) 
       (= 2 (apply max (vals (frequencies b)))))))

(defn three-of-a-kind? [c]
  (let [b (map get-value  c)]
   (= 3 (apply max (vals (frequencies b))))))

(defn straight? [c]
  (let [b (map get-value c)]
     (apply = true (map = b (range (first b) (+ (first b) (count b)))))))

(defn flush? [c] 
  (apply = (map second c)))

(defn full-house? [c]
  (let [b (map get-value  c)
        f (frequencies b)]
    (and (= 2 (count f))
      (or (= 2 (val (first f)))
          (= 3 (val (first f)))))))

(defn four-of-a-kind? [c]
  (let [b (map get-value  c)]
    (= 4 (apply max (vals (frequencies b))))))

(defn straight-flush? [c]
  (let [a (map get-value c)
        b (map second c)]
    (and (apply = true (map number? (map read-string (map str a))))
         (apply = true (map = a (range (first a) (+ (first a) (count a)))))
         (apply = b))))

(defn royal-flush? [c]
  (= (set (map first c)) #{\T \J \Q \K \A}))


;-----------PROCESSIING -----------

(defn evaluate [p] 
  (cond
    (royal-flush? p)     :royal-flush   
    (straight-flush? p)  :straight-flush 
    (four-of-a-kind? p)  :four-of-a-kind 
    (full-house? p)      :full-house     
    (flush? p)           :flush
    (straight? p)        :straight
    (three-of-a-kind? p) :three-of-a-kind
    (two-pairs? p)       :two-pairs
    (pair? p)            :pair           
    :else                :high-card))

(defn parse-line [line]
  (let [combinations (str/split line #" ")]
     {:p1 (take 5 combinations)
      :p2 (take-last 5 combinations)}))
   
(defn hand-compare [h1 h2]
  (let [h1val (.indexOf ranks (evaluate h1))
        h2val (.indexOf ranks (evaluate h2))]
  (if (or (= h1val h2val) (= :high-card (evaluate h1) (evaluate h2)))
    (high-card-value h1 h2)
    (if (> h1val h2val) :h1 :h2))))
    
(defn process [ln] 
  (let [players (parse-line ln)]
    (hand-compare (players :p1) (players :p2))))

(defn -main [& args]
  (let [results (atom [])]
    (doseq [line (line-seq (java.io.BufferedReader. *in*))] 
        (swap! results conj (process line)))
        (println (str "Player 01:" ((frequencies @results) :h1) " Hands"))
        (println (str "Player 02:" ((frequencies @results) :h2) " Hands"))))
