package powerdancer.asmproxy.examples;

import powerdancer.asmproxy.ClassRepo;
import powerdancer.asmproxy.ProxyClassGenerator;
import powerdancer.asmproxy.ProxyConstructor;
import powerdancer.asmproxy.utils.ByteCodeVersion;
import powerdancer.asmproxy.utils.MapClassLoader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ManualClassLoading_Example {
    public interface Mirror {
        int someInt(int i);
        String someString(String s);
    }

    public static void main(String[] dontCare) throws Throwable {
        Class<? extends Mirror> proxyClass = ProxyClassGenerator.generateProxyClass(
                myClassRepo(),
                ByteCodeVersion.VM_8,
                "MyProxyClassName",
                method-> args -> args.get(0),
                Void.class,
                Mirror.class
        );

        Mirror m = proxyClass.getConstructor(Void.class).newInstance((Void)null);

        System.out.println("m.someInt(5) = " + m.someInt(5));
        System.out.println("m.someString(\"helloWorld\") = " + m.someString("helloWorld"));
    }

    private static ClassRepo myClassRepo() throws Throwable {
        // implement ClassRepo by loading the byteCode in your own class loader.
        // code in this method implementation is only for illustration purposes, you are free to use any of your own class loaders

        Path tempDir = Files.createTempDirectory("");
        URLClassLoader myClassLoader = new URLClassLoader(new URL[]{tempDir.toUri().toURL()}, ClassLoader.getSystemClassLoader());

        return (className, byteCode) -> {
            try {
                Files.write(tempDir.resolve(className + ".class"), byteCode, StandardOpenOption.CREATE_NEW);
                return myClassLoader.loadClass(className);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
