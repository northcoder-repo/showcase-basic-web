package org.northcoder.titlewebdemo.util;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

/**
 *
 */
public class HttpStatusTest {
    
    @Test
    public void testStatusOK() {
        int status = 200;
        String result = HttpStatus.valueOf(String.format("_%s", status)).getStatus();
        assertThat(result).isEqualTo("OK");
    }
    
}
