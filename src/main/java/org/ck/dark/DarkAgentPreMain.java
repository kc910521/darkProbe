package org.ck.dark;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.ck.dark.sack.listener.MaAgentListener;
import org.ck.dark.sack.interceptor.InvokeChain;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @Author caikun
 * @Description main here
 * @Date 下午7:09 21-12-20
 **/
public class DarkAgentPreMain {


    private static String PACK_PATHS = null;

    private final static AgentBuilder.Listener listener = new MaAgentListener();

    private static List<String> ignoreMethodNames = new ArrayList<>(16);
    private static List<String> ignoreFullClass = new ArrayList<>(16);

    static {
        ignoreMethodNames.add("hashCode");
        ignoreMethodNames.add("compare");
//        ignoreMethodNames.add("run");


        ignoreFullClass.add("org.apache.zookeeper.server.Request");
    }

    public static void premain(String arg, Instrumentation inst) {
        PACK_PATHS = System.getProperty("agentTarget");
        System.out.println("HELLO! AGENT. target path is :" + PACK_PATHS);
        if (PACK_PATHS == null) {
            throw new RuntimeException("agentTarget param missed");
        }
        String[] split = PACK_PATHS.split(",");
        ElementMatcher.Junction<NamedElement> namedElementJunction = ElementMatchers.nameStartsWith(split[0]);
        for (int i = 1; i < split.length; i ++) {
            namedElementJunction = namedElementJunction.or(ElementMatchers.nameStartsWith(split[i]));
        }


        new AgentBuilder.Default().type(namedElementJunction).transform(transformer).with(listener).installOn(inst);
    }


    final static AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> {
        String ignoreMethodNames = System.getProperty("ignoreMethodNames");
        if (ignoreMethodNames != null) {
            DarkAgentPreMain.ignoreMethodNames.addAll(Arrays.asList(ignoreMethodNames.split(",")));
        }
        // default no main Method
        ElementMatcher.Junction<MethodDescription> last = ElementMatchers.isMain();
        for (String iname: DarkAgentPreMain.ignoreMethodNames) {
            last = last.or(ElementMatchers.hasMethodName(iname));
        }
        last = ElementMatchers.not(last);
        return builder.method(last).intercept(MethodDelegation.to(InvokeChain.class));
    };



}