module powerdancer.asmproxy {
    requires org.objectweb.asm;
    requires org.objectweb.asm.util;
    requires org.slf4j;

    exports powerdancer.asmproxy;
    exports powerdancer.asmproxy.utils;
    opens powerdancer.asmproxy.internal;
    opens powerdancer.asmproxy;

}