package powerdancer.asmproxy.examples.stateful;

import powerdancer.asmproxy.AsmProxy;
import powerdancer.asmproxy.DynamicConstructor;
import powerdancer.asmproxy.MapClassLoader;
import powerdancer.asmproxy.data.DataClass;

import static org.objectweb.asm.Opcodes.V11;

public class StatefulExample {
    public static void main(String[] dontCare) {
        DynamicConstructor<MyData> opsConstructor = AsmProxy.generateImplClass(
                MapClassLoader.systemInstance(),
                V11,
                method -> {
                    switch (method.getName()) {
                        case "a":
                            return (args -> args.instanceState().a());
                        case "b":
                            return (args -> args.instanceState().b());
                        case "incrementA":
                            return (args -> {
                                args.instanceState().a(args.instanceState().a() + 1);
                                return null;
                            });
                        default:
                            throw new IllegalArgumentException();
                    }
                },
                MyOperations.class
        );

        DynamicConstructor<?> dataConstructor = DataClass.generateImplClass(
                MapClassLoader.systemInstance(),
                V11,
                MyData.class
        );

        MyData d = dataConstructor.<MyData>apply()
                .a(5)
                .b(8);

        MyOperations o = opsConstructor.apply(d);

        System.out.println("d.a()=" + d.a());

        System.out.println("o.incrementA()");
        o.incrementA();

        System.out.println("d.a()=" + d.a());

        System.out.println("o.a()=" + o.a());
        System.out.println("o.b()=" + o.b());
    }

    public interface MyOperations {
        void incrementA();
        int a();
        int b();
    }

    public interface MyData {
        int a();
        MyData a(int a);

        int b();
        MyData b(int b);
    }
}
