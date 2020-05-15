package powerdancer.asmproxy.examples;

import powerdancer.asmproxy.ProxyConstructor;

public class ProxySelfReference_Example {

    public interface MyInt {
        int myInt();
        int myIntPlus1();
    }

    public static void main(String[] dontCare) throws Throwable {
        ProxyConstructor<MyInt, Integer> constructor = ProxyConstructor.generateProxyConstructor(
                method-> {
                    if (method.getName().equals("myInt")) return args->args.state();
                    return args->args.<MyInt>instance().myInt() + 1;
                },
                Integer.class,
                MyInt.class
        );

        MyInt i = constructor.apply(8);

        System.out.println("i.myInt() = " + i.myInt());
        System.out.println("i.myIntPlus1() = " + i.myIntPlus1());
    }
}
