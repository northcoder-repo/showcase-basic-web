package org.northcoder.titlewebdemo.util;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

/**
 *
 */
public class HttpStatusTest {

    @Test
    public void testStatusOK_a() {
        int status = 200;
        String result = HttpStatus.valueOf(String.format("_%s", status)).getStatusName();
        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testStatusOK_b() {
        int status = 200;
        int result = HttpStatus.valueOf(String.format("_%s", status)).getStatusCode();
        assertThat(result).isEqualTo(status);
    }

}
