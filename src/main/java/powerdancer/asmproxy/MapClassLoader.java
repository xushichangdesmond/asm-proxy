package powerdancer.asmproxy;

import powerdancer.asmproxy.internal.InterfaceUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MapClassLoader extends ClassLoader implements GenerationOps{

    private static MapClassLoader systemInstance = new MapClassLoader(ClassLoader.getSystemClassLoader());

    public MapClassLoader(ClassLoader parent) {
        super(parent);
    }

    final ConcurrentHashMap<String, Class> m = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, byte[]> rawM = new ConcurrentHashMap<>();


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class c = m.get(name);
        if (c != null) return c;
        return super.findClass(name);
    }

    public static MapClassLoader systemInstance() {
        return systemInstance;
    };

    @Override
    public Class addClass(String name, byte[] classPayload) {
//        TraceClassUtils.trace(classPayload, new PrintWriter(System.err));
//        try {
//            Files.write(
//                    Path.of("generated/" + name.replace('$', '.') + ".class"),
//                    classPayload,
//                    StandardOpenOption.CREATE_NEW,
//                    StandardOpenOption.WRITE
//            );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Class c = defineClass(name, classPayload, 0, classPayload.length);
        m.put(name, c);
        rawM.put(name, classPayload);
        try {
            loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new AsmProxyException("Unable to load class", e);
        }
        return c;
    }


}
