package org.ck.dark.sack.interceptor;

import net.bytebuddy.asm.Advice;
import org.ck.dark.sack.thread.CounterThreadLocalUtil;
import org.ck.dark.sack.util.BaseTool;

import java.lang.reflect.Method;

/**
 * @Author caikun
 * @Description
 * case from
 * public class MethodTracker {
 *
 *     @Advice.OnMethodEnter
 *     public static Object onMethodBegin(@Advice.This Object invokedObject, @Advice.AllArguments Object[] arguments,
 *                                        @Advice.FieldValue("name") Object fieldValue, @Advice.Origin Object origin,
 *                                        @Advice.Local("localVariable") Object localVariable) {
 *         System.out.println("=======on Method Begin Running with ByteBuddy=======, " + invokedObject);
 *         System.out.println("======Printing arguments=======");
 *         for(Object obj: arguments){
 *             System.out.println("Argument:: " + obj);
 *         }
 *         localVariable = "Gunika";
 *
 *         System.out.println("FieldValue:: " + fieldValue);
 *         System.out.println("Origin:: " + origin);
 *         return "ReturningStateFromOnMethodBegin";
 *     }
 *
 *     @Advice.OnMethodExit(onThrowable = Throwable.class, suppress = Throwable.class)
 *     public static void onMethodEnd(@Advice.This Object invokedObject, @Advice.Return Object returnValue,
 *                                    @Advice.FieldValue("name") Object fieldValue, @Advice.Enter Object state,
 *                                    @Advice.Local("localVariable") Object localVariable){
 *         System.out.println("=======on Method End Running with ByteBuddy======= " + invokedObject);
 *
 *         System.out.println("Return value is " + returnValue);
 *         System.out.println("FieldValue:: " + fieldValue);
 *         System.out.println("FieldValue:: " + fieldValue);
 *         System.out.println("State:: " + state);
 *         System.out.println("LocalVariable:: " + localVariable);
 *
 *
 *
 *
 * @Date 上午11:41 21-12-22
 **/
public class InvokeChain {

    private static final String formatTemplateHead =
            "↗---------↘↙---↖\n" +
            "[%s]go!↑↑ %s#%s(%s";
    private static final String formatTemplateBody = "[%s]-%s---> %s#%s(%s";

    private static final String appender = "--";
    // @SuperMethod
    // MethodCall
//    @RuntimeType
//    public static Object intercept(@Origin Method method,
//                                   @SuperCall Callable<?> callable) {
//        int afterIncr = CounterThreadLocalUtil.incr(1);
//        long tid = Thread.currentThread().getId();
//        if (afterIncr == 1) {
//            System.out.println(String.format(formatTemplateHead,
//                    Thread.currentThread().getName(),
//                    tid,
//                    method));
//        } else {
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < afterIncr; i++) {
//                sb.append("--");
//            }
//            System.out.println(String.format(formatTemplateBody,
//                    tid,
//                    sb.toString(),
//                    method));
//        }
//        try {
//            // 原有函数执行
//            return callable.call();
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        } finally {
//            CounterThreadLocalUtil.incr(-1);
//        }
//    }

    @Advice.OnMethodEnter
    public static void before(@Advice.Origin Method method) {
        int afterIncr = CounterThreadLocalUtil.incr(1);
        // todo: Invoking getThreadId may occur StackOverflow Exception
        Thread currThread = Thread.currentThread();
        String tName = currThread.getName();
        if (afterIncr == 1) {
            System.out.println(String.format(formatTemplateHead,
                    tName,
                    method.getDeclaringClass().getName(),
                    method.getName(),
                    BaseTool.stringifyTypes(method.getParameterTypes())
                    ));
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < afterIncr; i++) {
                sb.append(appender);
            }
            System.out.println(String.format(formatTemplateBody,
                    tName,
                    sb,
                    method.getDeclaringClass().getName(),
                    method.getName(),
                    BaseTool.stringifyTypes(method.getParameterTypes())
            ));
        }
    }



    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void after(@Advice.Thrown Throwable throwable) {
        CounterThreadLocalUtil.incr(-1);
    }



}
