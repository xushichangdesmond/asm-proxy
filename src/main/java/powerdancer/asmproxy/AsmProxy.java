package powerdancer.asmproxy;

import powerdancer.asmproxy.internal.InterfaceUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Function;

public interface AsmProxy {
    static <S> DynamicConstructor<S> generateImplClass(GenerationOps classGenerationOps, int version, Function<Method, MethodImpl<S>> implProvider, Class... interfaceClasses) {
        String name = UUID.randomUUID().toString();
        Class c = generateImplClass(
                classGenerationOps,
                version,
                name,
                implProvider,
                interfaceClasses
        );

        Constructor constructor;
        try {
            constructor = c.getConstructor(Object.class);
        } catch (Exception e) {
            throw new AsmProxyException("unexpected error", e);
        }

        return new DynamicConstructor<S>() {
            @Override
            public <T> T apply(S instanceState) {
                try {
                    return (T)constructor.newInstance(instanceState);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    throw new AsmProxyException("error constructing instance", e);
                }
            }
        };
    }

    static <S> Class generateImplClass(GenerationOps ops, int version, String name, Function<Method, MethodImpl<S>> implProvider, Class... interfaceClasses) {
        return InterfaceUtils.generateImplClass(
          ops,
          version,
          name,
          implProvider,
          interfaceClasses
        );
    }
}
