package powerdancer.asmproxy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

public interface TraceClassUtils {
    static void trace(byte[] byteCode, PrintWriter writer) {
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, new ASMifier(), writer);
        ClassReader cr = new ClassReader(byteCode);
        cr.accept(traceClassVisitor, 0);
    }
}
