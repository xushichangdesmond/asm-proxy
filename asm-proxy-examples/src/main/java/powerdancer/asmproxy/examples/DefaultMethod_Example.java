package powerdancer.asmproxy.examples;

import powerdancer.asmproxy.ProxyConstructor;

public class DefaultMethod_Example {

    public interface Name {
        String firstName();
        String lastName();

        default String fullName() {
            return firstName() + " " + lastName();
        }
    }

    public static void main(String[] dontCare) throws Throwable {
        ProxyConstructor<Name, Void> constructor = ProxyConstructor.generateProxyConstructor(
                method-> {
                    if (method.isDefault()) return null; // returning null means do not implement this method (but you can choose to if you prefer)
                    return args -> method.getName();
                },
                Void.class,
                Name.class
        );

        Name m = constructor.apply();

        System.out.println("m.firstName() = " + m.firstName());
        System.out.println("m.lastName() = " + m.lastName());
        System.out.println("m.fullName() = " + m.fullName());
    }
}
