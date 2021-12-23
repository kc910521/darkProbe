package org.ck.dark;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.ck.dark.sack.config.VMConfigExecutor;
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
public class DarkProbePreMain {


    private final static AgentBuilder.Listener listener = new MaAgentListener();

    private final static VMConfigExecutor vmConfigExecutor = new VMConfigExecutor();

    public static void premain(String arg, Instrumentation inst) {
        System.out.println("\n" +
                "            __\n" +
                "(\\,--------'()'--o\n" +
                " (_    ___    /~\"\n" +
                "  (_)_)  (_)_)\n" +
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~ DARK_PROBE");
        ElementMatcher.Junction<NamedElement> namedElementJunction = vmConfigExecutor.inspectPackageOr().and(
                vmConfigExecutor.ignorePackageAnd());
        new AgentBuilder.Default().type(namedElementJunction).transform(transformer).with(listener).installOn(inst);

    }


    final static AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> {
        ElementMatcher.Junction<MethodDescription> methodDescriptionJunction = vmConfigExecutor.ignoreMethodAnd();
        return builder.method(methodDescriptionJunction).intercept(Advice.to(InvokeChain.class));
    };


}