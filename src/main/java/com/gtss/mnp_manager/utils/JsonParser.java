package com.gtss.mnp_manager.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Helper Class to parse and stringify json
 */
public class JsonParser {

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T asDtoObject(final String jsonString,
            Class<T> valueType) {
        try {
            return new ObjectMapper().readValue(jsonString, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
