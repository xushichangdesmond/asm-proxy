package powerdancer.asmproxy;

/**
 * Implementors to define the class in their own class loader and load them
 */
@FunctionalInterface
public interface ClassRepo {
    Class addClass(String name, byte[] classPayload);
}