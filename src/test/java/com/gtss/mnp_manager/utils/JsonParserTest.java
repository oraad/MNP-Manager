package com.gtss.mnp_manager.utils;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class JsonParserTest {
    
    @Test
    void itShouldStringifyObjectAsJson() {

        String json = JsonParser.asJsonString(new int[]{1,2,3});
        assertThat(json).isEqualTo("[1,2,3]");
    }

    @Test
    void itShouldParseJsontoObject() {

        String json = "[1,2,3]";
        Integer[] arr = JsonParser.asDtoObject(json, Integer[].class);

        assertThat(arr).usingRecursiveComparison().isEqualTo(new int[]{1,2,3});
    }
}
