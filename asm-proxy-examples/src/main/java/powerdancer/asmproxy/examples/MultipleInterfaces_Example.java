package powerdancer.asmproxy.examples;

import powerdancer.asmproxy.ProxyConstructor;

public class MultipleInterfaces_Example {
    public interface A {
        String a();
    }

    public interface B {
        String b();
    }

    public interface C {
        String c();
    }

    public static void main(String[] dontCare) throws Throwable {
        ProxyConstructor<A, Void> constructor = ProxyConstructor.generateProxyConstructor(
                method-> args -> method.getName(),
                Void.class,
                A.class,
                B.class,
                C.class
        );

        A proxy = constructor.apply();

        System.out.println("proxy.a() = " + proxy.a());
        System.out.println("proxy.b() = " + ((B)proxy).b());
        System.out.println("proxy.c() = " + ((C)proxy).c());
    }
}
