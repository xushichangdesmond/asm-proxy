package powerdancer.asmproxy;

public interface Arguments<S> {

    @Deprecated
    <T> T internal_get(int index, Class<T> klass);

    int size();

    default String getString(int index) {
        return get(index, String.class);
    }

    default S instanceState() {
        return (S)internal_get(1, Object.class);
    }

    default <T> T instance() {
        return (T)internal_get(0, Object.class);
    }

    default <T> T get(int index, Class<T> klass) {
        return internal_get(index + 2, klass);
    }

}
