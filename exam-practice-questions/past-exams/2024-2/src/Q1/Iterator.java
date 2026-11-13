public interface Iterator<T> {

    boolean hasNext();

    T next();

    /** Not a standard iterator method. For exam testing only. */
    int size();
}
