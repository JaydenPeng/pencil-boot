package org.pencil.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * @author pencil
 * @date 24/10/01 18:33
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        setSerializable(JsonInclude.Include.ALWAYS);
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

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
            return Strings.EMPTY;
        }
    }

    public static JsonNode toJsonNode(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (Exception e) {
            log.warn("json str to JsonNode fail", e);
            return null;
        }
    }

    public static JsonNode toJsonNode(Object object) {
        try {
            return OBJECT_MAPPER.valueToTree(object);
        } catch (Exception e) {
            log.warn("json obj to JsonNode fail", e);
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

    public static <T> T toObject(JsonNode jsonNode, TypeReference<T> reference) {
        try {
            return OBJECT_MAPPER.convertValue(jsonNode, reference);
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
