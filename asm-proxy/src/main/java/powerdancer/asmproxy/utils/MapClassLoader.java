package powerdancer.asmproxy.utils;

import powerdancer.asmproxy.AsmProxyException;
import powerdancer.asmproxy.ClassRepo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple class loader implementation that can load a class from bytecode during runtime.
 * Impelements ClassRepo too for simple use via ProxyConstructor
 */
public class MapClassLoader extends ClassLoader implements ClassRepo {

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
