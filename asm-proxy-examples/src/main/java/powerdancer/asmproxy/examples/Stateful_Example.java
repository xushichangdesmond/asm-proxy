package powerdancer.asmproxy.examples;

import powerdancer.asmproxy.ProxyConstructor;

import java.util.concurrent.atomic.AtomicInteger;

public class Stateful_Example {
    public interface Counter {
        int count();
    }

    public static void main(String[] dontCare) throws Throwable {
        ProxyConstructor<Counter, AtomicInteger> constructor = ProxyConstructor.generateProxyConstructor(
                method-> args -> args.state().getAndIncrement(),
                AtomicInteger.class,
                Counter.class
        );

        Counter c = constructor.apply(new AtomicInteger());

        System.out.println("c.count() = " + c.count());
        System.out.println("c.count() = " + c.count());
        System.out.println("c.count() = " + c.count());
        System.out.println("c.count() = " + c.count());
    }
}
