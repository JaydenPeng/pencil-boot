package org.pencil.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pencil
 * @date 24/10/01 18:33
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void setSerializable(JsonInclude.Include include) {
        OBJECT_MAPPER.setSerializationInclusion(include);
    }

    public static ObjectMapper getJsonMapper() {
        return OBJECT_MAPPER;
    }

    public static String toJsonStr(Object object) {

        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            log.warn("json obj to str fail", e);
            return null;
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.warn("json str to obj fail", e);
            return null;
        }
    }

    public static ArrayNode toArrayNode(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, ArrayNode.class);
        } catch (Exception e) {
            log.warn("json str to arrayNode fail", e);
            return null;
        }
    }

}
