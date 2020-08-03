import java.util.Objects;

public class Pair<T, K> {

    private T first;
    private K second;

    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return this.first;
    }

    public K getSecond() {
        return this.second;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Pair) {
            return (((Pair) o).getFirst() == this.getFirst() &&
                    ((Pair) o).getSecond() == this.getSecond())
                    ||
                    (((Pair) o).getSecond() == this.getFirst() &&
                            ((Pair) o).getFirst() == this.getSecond());
        }
        return false;
    }
}
