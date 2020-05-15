package powerdancer.asmproxy;

import powerdancer.asmproxy.utils.ByteCodeVersion;
import powerdancer.asmproxy.utils.MapClassLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public interface ProxyConstructor<T, S> {
    T apply(S state);
    default T apply() {
        return apply(null);
    }

    Class proxyClass();
    Class<S> stateClass();
    Class<T> primaryInterface();

    static <T, S> ProxyConstructor<T, S> generateProxyConstructor(ClassRepo classRepo, int version, Function<Method, MethodImpl<S>> implProvider, Class<S> stateClass, Class<T> primaryInterface, Class... additionalInterfaceClasses) {
        String name = UUID.randomUUID().toString();
        Class c = ProxyClassGenerator.generateProxyClass(
                classRepo,
                version,
                name,
                implProvider,
                stateClass,
                Stream.concat(
                        Stream.of(primaryInterface),
                        Arrays.stream(additionalInterfaceClasses)
                ).toArray(Class[]::new)
        );

        Constructor constructor;
        try {
            constructor = c.getConstructor(stateClass);
        } catch (Exception e) {
            throw new AsmProxyException("unexpected error", e);
        }

        return new ProxyConstructor<T, S>() {
            @Override
            public T apply(S instanceState) {
                try {
                    return (T)constructor.newInstance(instanceState);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    throw new AsmProxyException("error constructing instance", e);
                }
            }

            @Override
            public Class proxyClass() {
                return c;
            }

            @Override
            public Class<T> primaryInterface() {
                return primaryInterface;
            }

            @Override
            public Class<S> stateClass() {
                return stateClass;
            }
        };
    }

    static <T, S> ProxyConstructor<T, S> generateProxyConstructor(int version, Function<Method, MethodImpl<S>> implProvider, Class<S> stateClass, Class<T> primaryInterface, Class... additionalInterfaceClasses) {
        return generateProxyConstructor(
                MapClassLoader.systemInstance(),
                version,
                implProvider,
                stateClass,
                primaryInterface,
                additionalInterfaceClasses
        );
    }

    static <T, S> ProxyConstructor<T, S> generateProxyConstructor(Function<Method, MethodImpl<S>> implProvider, Class<S> stateClass, Class<T> primaryInterface, Class... additionalInterfaceClasses) {
        return generateProxyConstructor(
                ByteCodeVersion.runtimeVersion(),
                implProvider,
                stateClass,
                primaryInterface,
                additionalInterfaceClasses
        );
    }
}
