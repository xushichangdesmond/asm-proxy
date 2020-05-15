package powerdancer.asmproxy.examples;

import powerdancer.asmproxy.ProxyConstructor;

public class Stateless_Example {

    public interface Mirror {
        int someInt(int i);
        String someString(String s);
    }

    public static void main(String[] dontCare) throws Throwable {
        ProxyConstructor<Mirror, Void> constructor = ProxyConstructor.generateProxyConstructor(
                method-> args -> args.get(0),
                Void.class,
                Mirror.class
        );

        Mirror m = constructor.apply();

        System.out.println("m.someInt(5) = " + m.someInt(5));
        System.out.println("m.someString(\"helloWorld\") = " + m.someString("helloWorld"));
    }
}
