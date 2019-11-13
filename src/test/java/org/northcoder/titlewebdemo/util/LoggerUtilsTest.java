package org.northcoder.titlewebdemo.util;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.BeforeClass;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class LoggerUtilsTest {
    
    public LoggerUtilsTest() {
    }

    @BeforeClass
    public static void beforeClass() {
    }

    @Test
    public void testDecodeHttpStatus404() {
        assertThat(LoggerUtils.decodeHttpStatus(404)).isEqualTo("Not Found");
    }

    @Test
    public void testDecodeHttpStatusUnknown() {
        assertThat(LoggerUtils.decodeHttpStatus(999)).isEqualTo("Unknown Status");
    }
    
    @Test
    public void testGetHttpRequestLogMessage() {
        // use mocks for servlet request and response objects:
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getRemoteAddr()).thenReturn("0:0:0:0:0:0:0:1");
        when(req.getMethod()).thenReturn("GET");
        when(req.getPathInfo()).thenReturn("/foo/bar");
        when(req.getQueryString()).thenReturn("baz=bat");
        when(req.getProtocol()).thenReturn("HTTP/1.1");
        when(resp.getStatus()).thenReturn(200);
        String msg = LoggerUtils.getHttpRequestLogMessage(req, resp, 1.5132F, "OK");
        String expected = "[Request] 0:0:0:0:0:0:0:1 \"GET /foo/bar?baz=bat HTTP/1.1\" status: [200 - OK] in 1.5132 ms";
        assertThat(msg).isEqualTo(expected);
    }

}
