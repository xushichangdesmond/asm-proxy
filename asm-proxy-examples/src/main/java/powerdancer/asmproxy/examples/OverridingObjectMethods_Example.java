package powerdancer.asmproxy.examples;

import powerdancer.asmproxy.ProxyConstructor;

public class OverridingObjectMethods_Example {
    public interface Mirror {
        int someInt(int i);
        String someString(String s);
    }

    public interface ToString {
        String toString();
    }

    public static void main(String[] dontCare) throws Throwable {
        ProxyConstructor<Mirror, Void> constructor = ProxyConstructor.generateProxyConstructor(
                method-> {
                    if (method.getDeclaringClass() == ToString.class) return args->"rooted";
                    return args -> args.get(0);
                },
                Void.class,
                Mirror.class,
                ToString.class
        );

        Mirror m = constructor.apply();

        System.out.println("m.someInt(5) = " + m.someInt(5));
        System.out.println("m.someString(\"helloWorld\") = " + m.someString("helloWorld"));
        System.out.println("m.toString() = " + m.toString());
    }
}
