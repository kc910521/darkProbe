package org.ck.dark.sack.interceptor;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import org.ck.dark.sack.thread.CounterThreadLocalUtil;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @Author caikun
 * @Description 方法计时
 * @Date 下午7:12 21-12-20
 **/
public class TimeInterceptor {

    private static final String formatTemplate = "%s|%s -> %f ms";

    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @SuperCall Callable<?> callable
                                   ) throws Exception {
        long start = System.nanoTime();
        int afterIncr = CounterThreadLocalUtil.incr(1);
        try {
            // 原有函数执行
            return callable.call();
        } finally {
            CounterThreadLocalUtil.incr(-1);
            if (afterIncr == 1) {
                System.out.println(String.format(formatTemplate,
                        Thread.currentThread().getName(),
                        method,
                        (System.nanoTime() - start) * 0.000001f));
            } else {
                System.out.println(String.format(formatTemplate,
                        afterIncr,
                        method,
                        (System.nanoTime() - start) * 0.000001f));
            }

        }
    }
}
