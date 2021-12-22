package org.ck.dark.sack.interceptor;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.ck.dark.sack.thread.CounterThreadLocalUtil;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @Author caikun
 * @Description
 * @Date 上午11:41 21-12-22
 **/
public class InvokeChain {

    private static final String formatTemplateHead = "[%s=%d]--> %s ";
    private static final String formatTemplateBody = "|-[%d] %s--> %s ";

    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @SuperCall Callable<?> callable
    ) throws Exception {
        int afterIncr = CounterThreadLocalUtil.incr(1);
        long tid = Thread.currentThread().getId();
        if (afterIncr == 1) {
            System.out.println(String.format(formatTemplateHead,
                    Thread.currentThread().getName(),
                    tid,
                    method));
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < afterIncr; i ++) {
                sb.append("--");
            }
            System.out.println(String.format(formatTemplateBody,
                    tid,
                    sb.toString(),
                    method));
        }
        try {
            // 原有函数执行
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            CounterThreadLocalUtil.incr(-1);
        }
    }
}
