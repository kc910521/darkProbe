package org.ck.dark.sack.util;

/**
 * @Author caikun
 * @Description
 * @Date 下午6:30 21-12-23
 **/
public class BaseTool {

    private BaseTool() {
    }


    public static String stringifyTypes(Class<?>[] types) {
        StringBuilder sb = new StringBuilder();
        for (int i = types.length - 1; i >= 0; i--) {
            sb.append(types[i].getSimpleName() + ", ");
        }
        return sb.toString();
    }


}
