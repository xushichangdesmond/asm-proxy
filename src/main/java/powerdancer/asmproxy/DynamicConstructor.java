package powerdancer.asmproxy;

public interface DynamicConstructor<S> {
    <T> T apply(S instanceState);

    default <T> T apply() {
        return apply(null);
    }
}
