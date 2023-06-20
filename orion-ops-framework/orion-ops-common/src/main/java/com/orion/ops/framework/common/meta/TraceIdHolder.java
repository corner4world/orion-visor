package com.orion.ops.framework.common.meta;

/**
 * traceId 持有者
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/16 17:35
 */
public class TraceIdHolder {

    private TraceIdHolder() {
    }

    /**
     * 请求序列
     */
    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();
    ;

    public static String get() {
        return HOLDER.get();
    }

    public static void set(String traceId) {
        HOLDER.set(traceId);
    }

    public static void remove() {
        HOLDER.remove();
    }

}
