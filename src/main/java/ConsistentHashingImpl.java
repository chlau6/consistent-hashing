import hash.HashFunction;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashingImpl<T> implements ConsistentHashing<T> {
    private final HashFunction hashFunction;

    private final int replicas;
    private final SortedMap<Integer, T> circle = new TreeMap<>();

    public ConsistentHashingImpl(HashFunction hashFunction, int replicas, Collection<T> nodes) {
        this.hashFunction = hashFunction;
        this.replicas = replicas;

        for (T node : nodes) {
            add(node);
        }
    }

    @Override
    public void add(T node) {
        // for loop is to add virtual node
        for (int i = 0; i < replicas; i++) {
            circle.put(hashFunction.hash(node.toString() + i), node);
        }
    }

    @Override
    public void remove(T node) {
        // for loop is to add virtual node
        for (int i = 0; i < replicas; i++) {
            circle.remove(hashFunction.hash(node.toString() + i));
        }
    }

    @Override
    public T get(Object key) {
        if (circle.isEmpty()) return null;

        int hash = hashFunction.hash(key);

        if (!circle.containsKey(hash)) {
            SortedMap<Integer, T> tailMap = circle.tailMap(hash);

            // check the location of next server
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }

        return circle.get(hash);
    }
}
