package powerdancer.asmproxy.examples.multipleInterfaces;

import powerdancer.asmproxy.AsmProxy;
import powerdancer.asmproxy.DynamicConstructor;
import powerdancer.asmproxy.MapClassLoader;

import static org.objectweb.asm.Opcodes.V11;

public class MultipleInterfacesExample {

    public static void main(String[] dontCare) {
        DynamicConstructor<String> constructor = AsmProxy.generateImplClass(
                MapClassLoader.systemInstance(),
                V11,
                method-> {
                    if (method.getName().equals("one")) return args->1;
                    return args->"two";
                },
                Interface1.class,
                Interface2.class
        );

        Interface1 h = constructor.apply();
        System.out.println(h.one());
        System.out.println(((Interface2)h).two());
    }

    public interface Interface1 {
        int one();
    }

    public interface Interface2 {
        String two();
    }
}
