package powerdancer.asmproxy;

import org.objectweb.asm.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.Opcodes.*;

public interface InterfaceUtils {

    interface Arguments {
        <T> T get(int index, Class<T> klass);
        int size();

        default String getString(int index) {
            return get(index, String.class);
        }
    }

    @FunctionalInterface
    interface MethodImpl {
        Object execute(Arguments args);
    }

    @FunctionalInterface
    interface GenerationOps {
        Class addClass(String name, byte[] classPayload);
    }

    static void generateImplClass(GenerationOps ops, int version, String name, Function<Method, MethodImpl> implProvider, Class... interfaceClasses) {
        Map<Integer, MethodImpl> toBootstrap = new HashMap<>();
        AtomicInteger funcCount = new AtomicInteger(0);

        ClassWriter w = new ClassWriter(COMPUTE_FRAMES);
        w.visit(
                version,
                ACC_PUBLIC,
                name,
                null,
                Type.getInternalName(Object.class),
                Arrays.stream(interfaceClasses).map(Type::getInternalName).toArray(String[]::new)
        );
        w.visitSource(name + ".java", null);

        w.visitInnerClass("powerdancer/asmproxy/InterfaceUtils$MethodImpl", "powerdancer/asmproxy/InterfaceUtils", "MethodImpl", ACC_PUBLIC | ACC_STATIC | ACC_ABSTRACT | ACC_INTERFACE);

        w.visitInnerClass("powerdancer/asmproxy/InterfaceUtils$Arguments", "powerdancer/asmproxy/InterfaceUtils", "Arguments", ACC_PUBLIC | ACC_STATIC | ACC_ABSTRACT | ACC_INTERFACE);

        {
            FieldVisitor fieldVisitor = w.visitField(ACC_FINAL | ACC_SYNTHETIC, "constructorArg", Type.getDescriptor(Object.class), null, null);
            fieldVisitor.visitEnd();
        }

        MethodVisitor mv = w.visitMethod(
                ACC_PUBLIC,
                "<init>",
                Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(Object.class)),
                null,
                null
        );
        mv.visitCode();
        Label labelA = new Label();
        mv.visitLabel(labelA);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V", false);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, name, "constructorArg", Type.getDescriptor(Object.class));
        mv.visitInsn(RETURN);
        Label labelB = new Label();
        mv.visitLabel(labelB);
        mv.visitLocalVariable("this","L" + name + ";", null, labelA, labelB, 0);
        mv.visitMaxs(2, 1);
        mv.visitEnd();


        Arrays.stream(interfaceClasses)
                .flatMap(
                        c-> Arrays.stream(c.getMethods())
                )
                .filter(Predicate.not(Method::isDefault))
                .forEach(method->{
                    int funcIndex = funcCount.incrementAndGet();

                    ClassWriter subClassWriter = new ClassWriter(COMPUTE_FRAMES);

                    subClassWriter.visit(V1_8, ACC_SUPER, name + "$" + funcIndex, null, "java/lang/Object", new String[] { Type.getInternalName(Arguments.class) });
                    subClassWriter.visitSource(name + ".java", null);

                    subClassWriter.visitOuterClass(name, method.getName(), Type.getMethodDescriptor(method));

                    subClassWriter.visitInnerClass(name + "$" + funcIndex, null, null, 0);

                    subClassWriter.visitInnerClass(Type.getInternalName(Arguments.class), Type.getInternalName(InterfaceUtils.class), "Arguments", ACC_PUBLIC | ACC_STATIC | ACC_ABSTRACT | ACC_INTERFACE);

                    Class<?>[] argTypes = Stream.concat(
                            Stream.of(Object.class),
                            Arrays.stream(method.getParameterTypes())
                    ).toArray(Class[]::new);

                    for (int i = 0; i < argTypes.length; i++) {
                        FieldVisitor fieldVisitor = subClassWriter.visitField(ACC_FINAL | ACC_SYNTHETIC, "val$arg" + i, Type.getDescriptor(argTypes[i]), null, null);
                        fieldVisitor.visitEnd();
                    }
                    {
                        FieldVisitor fieldVisitor = subClassWriter.visitField(ACC_FINAL | ACC_SYNTHETIC, "this$0", "L" + name + ";", null, null);
                        fieldVisitor.visitEnd();
                    }
                    {
                        MethodVisitor methodVisitor = subClassWriter.visitMethod(0, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, Stream.concat(Stream.of(Type.getType("L" + name + ";")), Arrays.stream(argTypes).map(Type::getType)).toArray(Type[]::new)), null, null);
                        methodVisitor.visitCode();


                        Label label0 = new Label();
                        methodVisitor.visitLabel(label0);

                        methodVisitor.visitVarInsn(ALOAD, 0);
                        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

                        methodVisitor.visitVarInsn(ALOAD, 0);
                        methodVisitor.visitVarInsn(ALOAD, 1);
                        methodVisitor.visitFieldInsn(PUTFIELD, name + "$" + funcIndex, "this$0", "L" + name + ";");

                        for (int i = 0; i < argTypes.length; i++) {
                            methodVisitor.visitVarInsn(ALOAD, 0);
                            methodVisitor.visitVarInsn(ALOAD, 2 + i);
                            methodVisitor.visitFieldInsn(PUTFIELD, name + "$" + funcIndex, "val$arg" + i, Type.getDescriptor(argTypes[i]));
                        }
                        methodVisitor.visitInsn(RETURN);
                        Label label1 = new Label();
                        methodVisitor.visitLabel(label1);
                        methodVisitor.visitLocalVariable("this", "L" + name + "$" + funcIndex + ";", null, label0, label1, 0);
                        methodVisitor.visitLocalVariable("this$0", "L" + name + ";", null, label0, label1, 1);
                        methodVisitor.visitMaxs(2, 2 + argTypes.length);
                        methodVisitor.visitEnd();
                    }
                    {
                        MethodVisitor methodVisitor = subClassWriter.visitMethod(ACC_PUBLIC, "get", "(ILjava/lang/Class;)Ljava/lang/Object;", "<T:Ljava/lang/Object;>(ILjava/lang/Class<TT;>;)TT;", null);
                        methodVisitor.visitCode();
                        Label[] labels = new Label[argTypes.length + 2];
                        labels[0]= new Label();
                        methodVisitor.visitLabel(labels[0]);
                        methodVisitor.visitVarInsn(ILOAD, 1);
                        for (int i = 0; i < argTypes.length; i++) {
                            labels[i + 1] = new Label();
                        }
                        labels[argTypes.length + 1] = new Label();
                        methodVisitor.visitLookupSwitchInsn(labels[argTypes.length + 1], IntStream.range(0, argTypes.length).toArray(), Arrays.stream(labels).skip(1).limit(argTypes.length).toArray(Label[]::new));
                        for (int i = 0; i < argTypes.length; i++) {
                            methodVisitor.visitLabel(labels[i + 1]);
                            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                            methodVisitor.visitVarInsn(ALOAD, 0);
                            methodVisitor.visitFieldInsn(GETFIELD, name + "$" + funcIndex, "val$arg" + i, Type.getDescriptor(argTypes[i]));
                            methodVisitor.visitInsn(ARETURN);
                        }
                        methodVisitor.visitLabel(labels[argTypes.length + 1]);
                        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                        methodVisitor.visitInsn(ACONST_NULL);
                        methodVisitor.visitInsn(ARETURN);
                        Label end = new Label();
                        methodVisitor.visitLabel(end);
                        methodVisitor.visitLocalVariable("this", "L" + name + "$" + funcIndex + ";", null, labels[0], end, 0);
                        methodVisitor.visitLocalVariable("index", "I", null, labels[0], end, 1);
                        methodVisitor.visitLocalVariable("klass", "Ljava/lang/Class;", "Ljava/lang/Class<TT;>;", labels[0], end, 2);
                        methodVisitor.visitMaxs(2, 3);
                        methodVisitor.visitEnd();
                    }
                    {
                        MethodVisitor methodVisitor = subClassWriter.visitMethod(ACC_PUBLIC, "size", "()I", null, null);
                        methodVisitor.visitCode();

                        methodVisitor.visitLdcInsn(argTypes.length);
                        methodVisitor.visitInsn(IRETURN);
                        methodVisitor.visitMaxs(1, 0);
                        methodVisitor.visitEnd();
                    }
                    subClassWriter.visitEnd();
                    ops.addClass(name + "$" + funcIndex, subClassWriter.toByteArray());

                    w.visitInnerClass(name +  "$" + funcIndex, null, null, 0);

                    FieldVisitor fieldVisitor = w.visitField(ACC_STATIC | ACC_PUBLIC, "func" + funcIndex, Type.getDescriptor(MethodImpl.class), null, null);
                    fieldVisitor.visitEnd();
                    toBootstrap.put(funcIndex, implProvider.apply(method));

                    MethodVisitor methodVisitor = w.visitMethod(ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
                    methodVisitor.visitCode();
                    Label label0 = new Label();
                    methodVisitor.visitLabel(label0);
                    methodVisitor.visitFieldInsn(GETSTATIC, name, "func" + funcIndex, Type.getDescriptor(MethodImpl.class));
                    methodVisitor.visitTypeInsn(NEW, name + "$" + funcIndex);
                    methodVisitor.visitInsn(DUP);
                    methodVisitor.visitVarInsn(ALOAD, 0);
                    methodVisitor.visitInsn(DUP);
                    methodVisitor.visitFieldInsn(GETFIELD, name, "constructorArg", Type.getDescriptor(Object.class));
                    for (int i = 1; i < argTypes.length; i++) {
                        methodVisitor.visitVarInsn(ALOAD, i);
                    }
                    methodVisitor.visitMethodInsn(INVOKESPECIAL, name + "$" + funcIndex, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, Stream.concat(Stream.of(Type.getType("L" + name + ";")), Arrays.stream(argTypes).map(Type::getType)).toArray(Type[]::new)), false);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(MethodImpl.class), "execute", Type.getMethodDescriptor(Type.getType(Object.class), Type.getType(Arguments.class)), true);

                    switch (Type.getReturnType(method).getSort()) {
                        case Type.BOOLEAN:
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
                            methodVisitor.visitInsn(IRETURN);
                            break;
                        case Type.BYTE:
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
                            methodVisitor.visitInsn(IRETURN);
                            break;
                        case Type.CHAR:
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Character");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
                            methodVisitor.visitInsn(IRETURN);
                            break;
                        case Type.SHORT:
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Short");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
                            methodVisitor.visitInsn(IRETURN);
                            break;
                        case Type.INT:
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                            methodVisitor.visitInsn(IRETURN);
                            break;
                        case Type.FLOAT:
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Float");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
                            methodVisitor.visitInsn(FRETURN);
                            break;
                        case Type.LONG:
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Long");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
                            methodVisitor.visitInsn(LRETURN);
                            break;
                        case Type.DOUBLE:
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Double");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
                            methodVisitor.visitInsn(DRETURN);
                            break;
                        default:
                            methodVisitor.visitTypeInsn(CHECKCAST, Type.getReturnType(method).getInternalName());
                            methodVisitor.visitInsn(ARETURN);
                            break;
                    }
                    Label label1 = new Label();
                    methodVisitor.visitLabel(label1);
                    methodVisitor.visitLocalVariable("this", "L" + name + ";", null, label0, label1, 0);
                    for (int i = 0; i < argTypes.length; i++) {
                        methodVisitor.visitLocalVariable("arg" + i, Type.getDescriptor(argTypes[i]), null, label0, label1, i + 1);
                    }
                    methodVisitor.visitMaxs(4 + argTypes.length, 1 + argTypes.length);
                    methodVisitor.visitEnd();
                });

        w.visitEnd();

        Class generated = ops.addClass(name, w.toByteArray());
        toBootstrap.entrySet().stream().forEach(
                e->{
                    try {
                        generated.getField("func" + e.getKey()).set(null, e.getValue());
                    } catch (IllegalAccessException | NoSuchFieldException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        );
    }

}
