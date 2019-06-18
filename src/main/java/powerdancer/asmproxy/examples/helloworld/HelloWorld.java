package powerdancer.asmproxy.examples.helloworld;

import powerdancer.asmproxy.InterfaceUtils;
import powerdancer.asmproxy.MapClassLoader;


import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.objectweb.asm.Opcodes.V1_8;

public class HelloWorld {

    public static void main(String[] startUpArgs) throws Throwable {

        MapClassLoader cl = new MapClassLoader(ClassLoader.getSystemClassLoader());
        InterfaceUtils.generateImplClass(
                (name, classPayload) -> cl.add(name, classPayload),
                V1_8,
                "SayHelloImpl",
                method-> (args -> args.getString(0) + " says " + IntStream.range(1, args.size()).mapToObj(i->args.getString(i)).collect(Collectors.joining(" "))),
                SayHello.class
        );
        Class loadedClass = cl.loadClass("SayHelloImpl");

        SayHello h = (SayHello) loadedClass.getConstructor(Object.class).newInstance(new Object[]{"powerdancer"});
        System.out.println(h.hello("hello", "world"));
        System.out.println(h.hello("hola"));
    }
}
