package hash;

public class HashFunctionImpl implements HashFunction {
    @Override
    public int hash(Object s) {
        return String.valueOf(s).hashCode();
    }
}
