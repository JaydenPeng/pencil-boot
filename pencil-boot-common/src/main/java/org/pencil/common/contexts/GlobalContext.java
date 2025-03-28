package org.pencil.common.contexts;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pencil
 * @date 2020/3/16 14:03
 */
public class GlobalContext {

    private static final TransmittableThreadLocal<Map<String, Object>> CONTEXT_HOLDER = TransmittableThreadLocal.withInitial(HashMap::new);

    public static Map<String, Object> get() {
        Map<String, Object> map = CONTEXT_HOLDER.get();
        if (map == null) {
            map = new HashMap<>();
            CONTEXT_HOLDER.set(map);
        }
        return map;
    }

    public static void put(String key, Object value) {
        get().put(key, value);
    }

    public static Object get(String key) {
        return get().get(key);
    }

    public static void clearAll() {
        if (CONTEXT_HOLDER.get() != null) {
            CONTEXT_HOLDER.remove();
            MDC.clear(); // 清理 MDC 中的内容
        }
    }

    public static void clear(String key) {
        get().remove(key);
    }

    // 生成traceid
    public static void generateTraceId(String traceId) {
        if (traceId == null || traceId.isEmpty()) {
            throw new IllegalArgumentException("traceId cannot be null or empty");
        }
        put("traceId", traceId);
        MDC.put("traceId", traceId);
    }


    // 私有构造
    private GlobalContext() {
    }

}
