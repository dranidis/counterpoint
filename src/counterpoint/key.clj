(ns counterpoint.key)

(defn make-key [key]
  key)

(defn get-key [key]
  key)

(def accidentals-map
  {:cb {:b :flat :e :flat :a :flat :d :flat :g :flat :c :flat :f :flat}
   :gb {:b :flat :e :flat :a :flat :d :flat :g :flat :c :flat}
   :db {:b :flat :e :flat :a :flat :d :flat :g :flat}
   :ab {:b :flat :e :flat :a :flat :d :flat}
   :eb {:b :flat :e :flat :a :flat}
   :bb {:b :flat :e :flat}
   :f {:b :flat}
   :c {}
   :g {:f :sharp}
   :d {:f :sharp :c :sharp}
   :a {:f :sharp :c :sharp :g :sharp}
   :e {:f :sharp :c :sharp :g :sharp :d :sharp}
   :b {:f :sharp :c :sharp :g :sharp :d :sharp :a :sharp}
   :f# {:f :sharp :c :sharp :g :sharp :d :sharp :a :sharp :e :sharp}
   :c# {:f :sharp :c :sharp :g :sharp :d :sharp :a :sharp :e :sharp :b :sharp}})

(defn get-note-acc-at-key [key note]
  (get (get accidentals-map (get-key key) {}) note :natural))


