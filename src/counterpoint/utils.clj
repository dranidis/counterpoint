(ns counterpoint.utils)

(defn rule-warning [rule message-fun]
  (when (not rule)
    (prn (message-fun)))
  rule)
