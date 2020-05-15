package powerdancer.asmproxy;

import powerdancer.asmproxy.internal.InternalProxyClassGenerator;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * For advanced users who want to provide their own classloader for the proxy class generation.
 */
public interface ProxyClassGenerator {
    static <S> Class generateProxyClass(
            ClassRepo classRepo,
            int version,
            String name,
            Function<Method, MethodImpl<S>> implProvider,
            Class<S> stateClass,
            Class... interfaceClasses) {
        return InternalProxyClassGenerator.generateProxyClass(
                classRepo,
                version,
                name,
                implProvider,
                stateClass,
                interfaceClasses
        );
    }
}