package powerdancer.asmproxy;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MapClassLoader extends ClassLoader {
    public MapClassLoader(ClassLoader parent) {
        super(parent);
    }

    final ConcurrentHashMap<String, Class> m = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, byte[]> rawM = new ConcurrentHashMap<>();

    public Class add(String name, byte[] byteCode) {
        Class c = defineClass(name, byteCode, 0, byteCode.length);
        m.put(name, c);
        rawM.put(name, byteCode);
        return c;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return Optional.ofNullable(m.get(name)).orElse(super.findClass(name));
    }
}
