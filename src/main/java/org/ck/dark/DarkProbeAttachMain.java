package org.ck.dark;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.matcher.ElementMatcher;
import org.ck.dark.sack.config.VMConfigExecutor;
import org.ck.dark.sack.interceptor.InvokeChain;
import org.ck.dark.sack.listener.MaAgentListener;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

/**
 * @Author caikun
 * @Description attach
 * @Date 下午5:04 21-12-23
 **/
public class DarkProbeAttachMain {

    private static AgentBuilder.Listener listener;

    private static VMConfigExecutor vmConfigExecutor;

    private static String banner = "\n" +
            "            __\n" +
            "(\\,--------'()'--o\n" +
            " (_    ___    /~\"\n" +
            "  (_)_)  (_)_)  .<n.\n";

    public static void agentmain(final String agentArgs,
                                 final Instrumentation inst) {
        listener = new MaAgentListener();
        vmConfigExecutor = new VMConfigExecutor(agentArgs);
        final AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> {
            ElementMatcher.Junction<MethodDescription> methodDescriptionJunction = vmConfigExecutor.ignoreMethodAnd();
            return builder.method(methodDescriptionJunction).intercept(Advice.to(InvokeChain.class));
        };
        System.out.println(
                DarkProbeAttachMain.banner + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~ DARK_PROBE");
        ElementMatcher.Junction<NamedElement> namedElementJunction = vmConfigExecutor.inspectPackageOr().and(
                vmConfigExecutor.ignorePackageAnd());


//        final AgentBuilder.Transformer transformer =
//                (b, typeDescription) -> b.method(ElementMatchers.named("setSecurityManager"))
//                        .intercept(MethodDelegation.to(MySystemInterceptor.class));

        // Disable a bunch of stuff and turn on redefine as the only option
        final ByteBuddy byteBuddy = new ByteBuddy().with(Implementation.Context.Disabled.Factory.INSTANCE);
        new AgentBuilder.Default()
                .with(byteBuddy)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .type(namedElementJunction)
                .transform(transformer).installOn(inst);







//        new AgentBuilder.Default().type(namedElementJunction).transform(transformer).with(listener).installOn(inst);

    }

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        VirtualMachine virtualMachine = VirtualMachine.attach("12343");
        String params = "inspectPackage=org.apache.zookeeper.server";
        virtualMachine.loadAgent("/home/caikun/IdeaProjects/dark_agent/target/dark-probe.jar=" + params);

    }
}
