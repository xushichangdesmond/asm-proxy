package powerdancer.asmproxy.internal;

import powerdancer.asmproxy.Arguments;

public interface Scope<S> extends Arguments<S> {
    <T> T internal_get(int index, Class<T> type);

    @Override
    default S state() {
        return (S)internal_get(1, Object.class);
    }

    @Override
    default <T> T instance() {
        return (T)internal_get(0, Object.class);
    }

    @Override
    default <T> T get(int index, Class<T> type) {
        return internal_get(index + 2, type);
    }
}
