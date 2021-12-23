package org.ck.dark.sack.config;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public VMConfigExecutor() {
        load();
    }

    public ElementMatcher.Junction<NamedElement> inspectPackageOr() {
        return inspectPackage.stream().map(ElementMatchers::nameStartsWith).reduce((x, y) -> x.or(y)).get();
    }

    public ElementMatcher.Junction<NamedElement> ignorePackageAnd() {
        return ElementMatchers.not(ignorePackage.stream().map(ElementMatchers::nameContainsIgnoreCase).reduce((x, y) -> x.or(y)).get());
    }

    public ElementMatcher.Junction<MethodDescription> ignoreMethodAnd() {
        return ElementMatchers.not(ignoreMethods.stream().map(ElementMatchers::hasMethodName).reduce((x, y) -> x.or(y)).get());
    }


    /**
     * load data from System VM
     */
    protected void load() {
        String property1 = System.getProperty(VM_KEY_NEED_PACKAGE);
        String property2 = System.getProperty(VM_KEY_IGNORE_PACKAGE);
        String property3 = System.getProperty(VM_KEY_IGNORE_METHOD);
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
