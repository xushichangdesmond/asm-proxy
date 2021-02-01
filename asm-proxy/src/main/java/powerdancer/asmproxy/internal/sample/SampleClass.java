package powerdancer.asmproxy.internal.sample;

import powerdancer.asmproxy.MethodImpl;
import powerdancer.asmproxy.internal.Scope;

public class SampleClass implements SampleInterface {

    final SampleState state;

    public SampleClass(SampleState state) {
        this.state = state;
    }

    class SampleClass$0 implements Scope {
        Object val$0;
        SampleState val$1;
        int val$2;
        String val$3;
        SampleClass$0(Object o, SampleState ss, int i, String s) {
            val$0 = o;
            val$1 = ss;
            val$2 = i;
            val$3 = s;
        }

        @Override
        public Object internal_get(int index, Class type) {
            switch (index) {
                case 0: return val$0;
                case 1: return val$1;
                case 2: return val$2;
                case 3: return val$3;
            }
            return null;
        }

        @Override
        public int size() {
            return 2;
        }
    }
    static MethodImpl func0;

    @Override
    public int someMethod(int i, String s) {
        return ((Integer)func0.execute(new SampleClass$0(this, state, i, s))).intValue();
    }


    class SampleClass$1 implements Scope {
        Object val$0;
        SampleState val$1;
        float val$2;

        SampleClass$1(Object o, SampleState ss, float f) {
            val$0 = o;
            val$1 = ss;
            val$2 = f;
        }

        @Override
        public Object internal_get(int index, Class type) {
            switch (index) {
                case 0: return val$0;
                case 1: return val$1;
                case 2: return val$2;
            }
            return null;
        }

        @Override
        public int size() {
            return 1;
        }
    }
    static MethodImpl func1;

    @Override
    public String anotherMethod(float f) {
        return ((String)func1.execute(new SampleClass$1(this, state, f)));
    }
}