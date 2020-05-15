package powerdancer.asmproxy.examples;

import powerdancer.asmproxy.ProxyConstructor;

public class MultipleInterfaces_Alternative_Example {
    public interface A {
        String a();
    }

    public interface B {
        String b();
    }

    public interface C {
        String c();
    }

    public interface Composite extends A,B,C{}

    public static void main(String[] dontCare) throws Throwable {
        ProxyConstructor<Composite, Void> constructor = ProxyConstructor.generateProxyConstructor(
                method-> args -> method.getName(),
                Void.class,
                Composite.class
        );

        Composite proxy = constructor.apply();

        System.out.println("proxy.a() = " + proxy.a());
        System.out.println("proxy.b() = " + proxy.b());
        System.out.println("proxy.c() = " + proxy.c());
    }
}
