package powerdancer.asmproxy.examples.employee;

import powerdancer.asmproxy.InterfaceUtils;
import powerdancer.asmproxy.MapClassLoader;


import static org.objectweb.asm.Opcodes.V1_8;

public class Example {
    public static void main(String[] mainArgs) throws Throwable {
        MapClassLoader cl = new MapClassLoader(ClassLoader.getSystemClassLoader());
        InterfaceUtils.generateImplClass(
                (name, classPayload) -> cl.add(name, classPayload),
                V1_8,
                "EmployeeImpl",
                method-> (args -> 10L),
                Employee.class
        );
        Class loadedClass = cl.loadClass("EmployeeImpl");

        Employee e = (Employee) loadedClass.getConstructor(Object.class).newInstance(new Object[]{"powerdancer"});
        System.out.println(e.id());
    }
}
