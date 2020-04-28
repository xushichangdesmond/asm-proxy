package powerdancer.asmproxy.examples.data;

import powerdancer.asmproxy.DynamicConstructor;
import powerdancer.asmproxy.MapClassLoader;
import powerdancer.asmproxy.data.DataClass;

import static org.objectweb.asm.Opcodes.V11;

public class DataInterfaceExample {
    public static interface MyData {
        int i();
        MyData i(int i);

        String s();
        MyData s(String s);
    }

    public static void main(String[] args) {
        DynamicConstructor<?> constructor = DataClass.generateImplClass(
                MapClassLoader.systemInstance(),
                V11,
                MyData.class
        );
        MyData d = constructor.<MyData>apply()
                .i(5)
                .s("my number is ");
        System.out.println(d.s() + d.i());
    }
}
