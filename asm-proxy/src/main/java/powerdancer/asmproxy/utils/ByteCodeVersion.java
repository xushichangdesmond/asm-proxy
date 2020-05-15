package powerdancer.asmproxy.utils;

import org.objectweb.asm.Opcodes;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public interface ByteCodeVersion {
    int VM_8 = Opcodes.V1_8;
    int VM_9 = Opcodes.V9;
    int VM_10 = Opcodes.V10;
    int VM_11 = Opcodes.V11;
    int VM_12 = Opcodes.V12;
    int VM_13 = Opcodes.V13;
    int VM_14 = Opcodes.V14;

    static int forMajorAndMinorClassFileVersion(int major, int minor) {
        return minor << 16 | major;
    }

    static int runtimeVersion() {
        try {
            Class runtimeVersionClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.Runtime$Version");
            Object v = MethodHandles.lookup().findStatic(Runtime.class, "version", MethodType.methodType(runtimeVersionClass))
                    .invoke();
            int feature = (int)MethodHandles.lookup().findVirtual(Runtime.class, "feature", MethodType.methodType(int.class))
                    .invoke(v);
            return 0 << 16 | (44 + feature);
        } catch (Throwable t) {
            return Opcodes.V1_8;
        }
    }
}
