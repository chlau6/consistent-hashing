public interface ConsistentHashing<T> {
    void add(T node);

    void remove(T node);

    T get(Object key);
}
