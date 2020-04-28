package powerdancer.asmproxy.examples.helloWorld.simplifiedExample;

import powerdancer.asmproxy.AsmProxy;
import powerdancer.asmproxy.DynamicConstructor;
import powerdancer.asmproxy.MapClassLoader;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.objectweb.asm.Opcodes.V11;
import static org.objectweb.asm.Opcodes.V1_8;

public class HelloWorld {

    public static void main(String[] startUpArgs) throws Throwable {

        DynamicConstructor<String> constructor = AsmProxy.generateImplClass(
                MapClassLoader.systemInstance(),
                V11,
                method-> (args -> args.instanceState() + " says " + IntStream.range(0, args.size()).mapToObj(i->args.getString(i)).collect(Collectors.joining(" "))),
                SayHello.class
        );

        SayHello h = constructor.apply("powerDancer");
        System.out.println(h.hello("hello", "world"));
        System.out.println(h.hello("hola"));
    }
}
