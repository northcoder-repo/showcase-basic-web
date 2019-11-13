package org.northcoder.titlewebdemo.validation;

import static com.google.common.truth.Truth.assertThat;
import java.util.Map;
import java.util.LinkedHashMap;
import org.junit.Test;

/**
 *
 */
public class ValidationHandlerTest {
    
    public ValidationHandlerTest() {
    }

    @Test
    public void testGetValidationErrors() {
    }

    @Test
    public void testErrorsToJson() {
        Map<String, String> errorsMap = new LinkedHashMap<>();
        errorsMap.put("Name 1", "Value 1");
        errorsMap.put("Name 2", "Value 2");
        String errorsJson = ValidationHandler.errorsToJson(errorsMap);
        String expectedJson = "{\"Name 1\":\"Value 1\",\"Name 2\":\"Value 2\"}";
        assertThat(errorsJson).isEqualTo(expectedJson);
    }
    
    @Test
    public void testErrorsFromJson() {
        String errorsJson = "{\"Name 1\":\"Value 1\",\"Name 2\":\"Value 2\"}";
        Map<String, String> errorsMap = ValidationHandler.errorsFromJson(errorsJson);
        Map<String, String> expectedMap = new LinkedHashMap<>();
        expectedMap.put("Name 1", "Value 1");
        expectedMap.put("Name 2", "Value 2");        
        assertThat(errorsMap).isEqualTo(expectedMap);
    }
    
}
