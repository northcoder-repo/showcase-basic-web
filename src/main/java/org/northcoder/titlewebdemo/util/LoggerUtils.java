package org.northcoder.titlewebdemo.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.common.base.Enums;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Handles automatic logging of all HTTP requests, and formatting of log messages.
 */
public class LoggerUtils {
    
    public static final Logger LOGGER = LogManager.getRootLogger();

    public static void logHttpRequest(HttpServletRequest req, HttpServletResponse resp, float millis) {
        String logMessage = getHttpRequestLogMessage(req, resp, millis, decodeHttpStatus(resp.getStatus()));
        LOGGER.info(logMessage);
    }

    public static String getHttpRequestLogMessage(HttpServletRequest req,
            HttpServletResponse resp, Float millis, String statusMsg) {
        // example log message:
        // [Request] 0:0:0:0:0:0:0:1 "GET /foo/bar?baz=bat HTTP/1.1" status 404 in 1.5132 ms
        StringBuilder sb = new StringBuilder();
        sb.append("[Request] ")
                .append(req.getRemoteAddr()).append(" \"")
                .append(req.getMethod()).append(" ")
                .append(req.getPathInfo());
        if (req.getQueryString() != null) {
            sb.append("?").append(req.getQueryString());
        }
        sb.append(" ").append(req.getProtocol()).append("\" status: [")
                .append(resp.getStatus()).append(" - ")
                .append(statusMsg)
                .append("] in ")
                .append(millis).append(" ms");
        return sb.toString();
    }

    public static String decodeHttpStatus(int status) {
        // decode from a number (e.g. 200) to a description (e.g. "OK").
        return Enums.getIfPresent(HttpStatus.class,
                String.format("_%s", status))
                .or(HttpStatus.UNKNOWN).getStatusName();
    }
    
    //private static void changeLogLevel(Level level) {
        // TODO - implement this!
        //Configurator.setAllLevels(LogManager.getRootLogger().getName(), level);
    //}
    
}
