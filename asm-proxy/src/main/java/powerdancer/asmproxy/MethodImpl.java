package powerdancer.asmproxy;

@FunctionalInterface
public interface MethodImpl<S> {
    Object execute(Arguments<S> args);
}
