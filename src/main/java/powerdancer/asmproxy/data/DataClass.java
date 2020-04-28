package powerdancer.asmproxy.data;

import powerdancer.asmproxy.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public interface DataClass {

    class DataState {
        final Object[] state;

        public DataState(int size) {
            state = new Object[size];
        }
    }

    static DynamicConstructor<?> generateImplClass(
        GenerationOps classGenerationOps,
        int version,
        Class dataInterfaceClass
    ) {
        AtomicInteger i = new AtomicInteger(0);
        List<String> propertyNames = new ArrayList<>();
        String name = UUID.randomUUID().toString();

        Class c = AsmProxy.generateImplClass(
                classGenerationOps,
                version,
                name,
                method -> {
                    String propName = method.getName();
                    int index = propertyNames.indexOf(propName);
                    int propIndex;
                    if (index == -1) {
                        propertyNames.add(propName);
                        propIndex = i.getAndIncrement();
                    } else propIndex = index;
                    if (method.getParameterCount() == 0) {
                        return args-> ((Object[])args.instanceState())[propIndex];
                    }
                    return args -> {
                        ((Object[])args.instanceState())[propIndex] = args.get(0, Object.class);
                        return args.instance();
                    };
                },
                dataInterfaceClass
        );

        Constructor constructor;
        try {
            constructor = c.getConstructor(Object.class);
        } catch (Exception e) {
            throw new AsmProxyException("unexpected error", e);
        }

        int size = i.get();
        return new DynamicConstructor<Void>() {
            @Override
            public <T> T apply(Void instanceState) {
                return apply();
            }

            @Override
            public <T> T apply() {
                try {
                    return (T)constructor.newInstance(new Object[]{new Object[size]});
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    throw new AsmProxyException("error constructing instance", e);
                }
            }
        };
    };


}