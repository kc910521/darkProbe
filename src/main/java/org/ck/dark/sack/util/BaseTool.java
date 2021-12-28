package org.ck.dark.sack.util;

import java.util.StringJoiner;

/**
 * @Author caikun
 * @Description
 * @Date 下午6:30 21-12-23
 **/
public class BaseTool {

    private BaseTool() {
    }

    /**
     * join to `(a, b, c)`
     * @param types
     * @return
     */
    public static String stringifyTypes(Class<?>[] types) {
        StringJoiner sj = new StringJoiner(", ", "(", ")");
        for (int i = types.length - 1; i >= 0; i--) {
            sj.add(types[i].getSimpleName());
        }
        return sj.toString();
    }


}
