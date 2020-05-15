package powerdancer.asmproxy;

public interface Arguments<S> {

    int size();
    S state();
    <T> T instance();
    <T> T get(int index, Class<T> type);

    default Object get(int index) {
        return get(index, Object.class);
    }
    default String getString(int index) {
        return get(index, String.class);
    }
    default int getInt(int index) {
        return get(index, int.class);
    }
    default Integer getIntObj(int index) {
        return get(index, Integer.class);
    }
}
