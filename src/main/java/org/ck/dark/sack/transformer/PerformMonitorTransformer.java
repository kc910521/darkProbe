//package org.ck.dark.sack.transformer;
//
//import javassist.CannotCompileException;
//import javassist.ClassPool;
//import javassist.CtBehavior;
//import javassist.CtClass;
//import javassist.expr.ExprEditor;
//import javassist.expr.MethodCall;
//
//import java.lang.instrument.ClassFileTransformer;
//import java.lang.instrument.IllegalClassFormatException;
//import java.security.ProtectionDomain;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * @Author caikun
// * @Description 原始的javaagent
// * from
// * https://blog.csdn.net/f59130/article/details/78481594
// *
// * @Date 下午6:45 21-12-20
// **/
//public class PerformMonitorTransformer implements ClassFileTransformer {
//
//    private static final Set<String> classNameSet = new HashSet<>();
//
//    static {
//        classNameSet.add("ind.ck.concurrent.basic.MyLock");
//    }
//
//    @Override
//    public byte[] transform(ClassLoader loader,
//                            String className,
//                            Class<?> classBeingRedefined,
//                            ProtectionDomain protectionDomain,
//                            byte[] classfileBuffer) throws IllegalClassFormatException {
//        try {
//            String currentClassName = className.replaceAll("/", ".");
//            System.out.println("e: [" + currentClassName + "]");
//            // 仅仅提升Set中含有的类
//            if (!classNameSet.contains(currentClassName)) {
//                return null;
//            }
//            System.out.println("transform: [" + currentClassName + "]");
//
//            CtClass ctClass = ClassPool.getDefault().get(currentClassName);
//            CtBehavior[] methods = ctClass.getDeclaredBehaviors();
//            for (CtBehavior method : methods) {
//                enhanceMethod(method);
//            }
//            return ctClass.toBytecode();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private void enhanceMethod(CtBehavior method) throws Exception {
//        System.out.println("CtBehavior:" + method.getLongName());
//        if (method.isEmpty()) {
//            return;
//        }
//        String methodName = method.getName();
//        if (methodName.equalsIgnoreCase("main")) { // 不提升main方法
//            return;
//        }
//
//        final StringBuilder source = new StringBuilder();
//        source.append("{")
//                .append("long start = System.nanoTime();\n") // 前置增强: 打入时间戳
//                .append("$_ = $proceed($$);\n") // 保留原有的代码处理逻辑
//                .append("System.out.print(\"method:[" + methodName + "]\");").append("\n")
//                .append("System.out.println(\" cost:[\" +(System.nanoTime() -start)+ \"ns]\");") // 后置增强
//                .append("}");
//
//        ExprEditor editor = new ExprEditor() {
//            @Override
//            public void edit(MethodCall methodCall) throws CannotCompileException {
//                methodCall.replace(source.toString());
//            }
//        };
//        method.instrument(editor);
//    }
//}