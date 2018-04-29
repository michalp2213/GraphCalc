package Model;

/*
 * Class describing vertex.
 */

public class Vertex<T> {
    private final T v;
    public Vertex (T v){
        this.v = v;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Vertex)) return false;
        return ((Vertex) obj).v.equals(v);
    }

    @Override
    public int hashCode() {
        return v.hashCode();
    }
}
