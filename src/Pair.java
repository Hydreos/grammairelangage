import java.util.*;

//Source : ChatGPT
class Pair<T> {
    final T first;
    final T second;

    public Pair(T first, T second) {
        // Assure que les objets sont stockés dans un ordre cohérent (pour éviter les permutations)
        if (first.hashCode() <= second.hashCode()) {
            this.first = first;
            this.second = second;
        } else {
            this.first = second;
            this.second = first;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?> pair = (Pair<?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}