package org.ck.dark.sack.config;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author caikun
 * @Description VM params init
 * @Date 下午3:52 21-12-23
 **/
public class VMConfigExecutor {

    /**
     * value must not null
     */
    private final static String VM_KEY_NEED_PACKAGE = "inspectPackage";

    private final static String VM_KEY_IGNORE_PACKAGE = "ignorePackage";

    private final static String VM_KEY_IGNORE_METHOD = "ignoreMethod";

    /**
     * fixme:not in using now
     */
    private final static String VM_KEY_NEED_METHOD = "inspectMethod";
    /**
     * =============================================================
     */
    private static List<String> inspectPackage = new ArrayList<>(10);
    private static List<String> ignorePackage = new ArrayList<>(10);
    private static List<String> ignoreMethods = new ArrayList<>(10);



    static {
        // set default ignore
        ignoreMethods.add("hashCode");
        ignoreMethods.add("compare");

        ignorePackage.add("$");
        ignorePackage.add("org.ck.dark");

    }

    public VMConfigExecutor(String agentParams) {
        loadFrom(agentParams);
    }

    public ElementMatcher.Junction<NamedElement> inspectPackageOr() {
        return inspectPackage.stream().map(ElementMatchers::nameStartsWith).reduce(ElementMatcher.Junction::or).get();
    }

    public ElementMatcher.Junction<NamedElement> ignorePackageAnd() {
        return ElementMatchers.not(ignorePackage.stream().map(ElementMatchers::nameContainsIgnoreCase).reduce(ElementMatcher.Junction::or).get());
    }

    public ElementMatcher.Junction<MethodDescription> ignoreMethodAnd() {
        return ElementMatchers.not(ignoreMethods.stream().map(ElementMatchers::hasMethodName).reduce(ElementMatcher.Junction::or).get());
    }

    /**
     * split a:123&b=uu
     * to
     * map {a=123}, {b=uu}
     *
     * @param agentParams
     */
    protected void loadFrom(String agentParams) {
        Map<String, String> properties = Stream.of(agentParams.trim().split("&")).map(s -> s.split("=")).collect(Collectors.toMap(d2 -> d2[0], d2 -> d2[1]));
        System.out.println("params load: " + properties);
        load(properties.get(VM_KEY_NEED_PACKAGE), properties.get(VM_KEY_IGNORE_PACKAGE), properties.get(VM_KEY_IGNORE_METHOD));
    }

    /**
     * load data from params VM
     */
    protected void load(String property1, String property2, String property3) {
        if (!Objects.isNull(property1) && !Objects.equals("", property1)) {
            inspectPackage.addAll(Arrays.asList(property1.split(",")));
        } else {
            throw new RuntimeException("VM OPTIONS: " + VM_KEY_NEED_PACKAGE + " MUST NOT NULL");
        }

        if (!Objects.isNull(property2) && !Objects.equals("", property2)) {
            ignorePackage.addAll(Arrays.asList(property2.split(",")));
        }
        if (!Objects.isNull(property3) && !Objects.equals("", property3)) {
            ignoreMethods.addAll(Arrays.asList(property3.split(",")));
        }
    }




}
