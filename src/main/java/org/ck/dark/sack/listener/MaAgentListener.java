package org.ck.dark.sack.listener;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

/**
 * @Author caikun
 * @Description 加载时判定需要代理的类
 *
 * 启动时修改主要是在jvm启动时，执行native函数的Agent_OnLoad方法，在方法执行时，执行如下步骤：
 *
 * 创建InstrumentationImpl对象
 *
 * 监听ClassFileLoadHook事件
 *
 * 调用InstrumentationImpl的loadClassAndCallPremain方法，在这个方法里会去调用javaagent里MANIFEST.MF里指定的Premain-Class类的premain方法
 *
 *
 *
 *
 *
 * 运行时修改主要是通过jvm的attach机制来请求目标jvm加载对应的agent，执行native函数的Agent_OnAttach方法，在方法执行时，执行如下步骤：
 *
 * 创建InstrumentationImpl对象
 *
 * 监听ClassFileLoadHook事件
 *
 * 调用InstrumentationImpl的loadClassAndCallAgentmain方法，在这个方法里会去调用javaagent里MANIFEST.MF里指定的Agentmain-Class类的agentmain方法
 *
 *
 * @Date 上午10:53 21-12-22
 **/
public class MaAgentListener implements AgentBuilder.Listener {

    // 所有方法调用
    @Override
    public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {
        System.out.println("onDiscovery:" + s);
    }
    // 目标
    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {
        System.out.println("onTransformation:" + typeDescription);

    }
    // 非目标
    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {
        System.out.println("onIgnored:" + typeDescription);

    }

    @Override
    public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {
        System.out.println("onError:" + s);
    }
    // 所有方法调用
    @Override
    public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {
        System.out.println("onComplete:" + s);


    }
}
