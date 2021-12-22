package org.ck.dark.sack.thread;

/**
 * @Author caikun
 * @Description 隔离计数
 * @Date 下午4:43 21-12-21
 **/
public class CounterThreadLocalUtil {

    private final static ThreadLocal<Integer> tl = ThreadLocal.withInitial(() -> 0);

    public static void clear() {
        tl.remove();
    }

    public static int get() {
        Integer count = tl.get();
        return count.intValue();
    }

    public static int incr(int add) {
        Integer count = tl.get();
        int result = count.intValue() + add;
        tl.set(result);
        return result;

    }


}
