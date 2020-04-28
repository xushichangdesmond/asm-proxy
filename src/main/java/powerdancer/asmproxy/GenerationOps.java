package powerdancer.asmproxy;

@FunctionalInterface
public interface GenerationOps {
    Class addClass(String name, byte[] classPayload);
}
