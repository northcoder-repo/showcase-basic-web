package org.northcoder.titlewebdemo.util;

import org.eclipse.jetty.server.session.JDBCSessionDataStoreFactory;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.server.session.SessionCache;
import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.DatabaseAdaptor;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Configures the Jetty built-in session tracker. Specifically, it uses a
 * database table called "jettysessions" to store information about each user
 * session, including session ID, expiry time, etc. If the "jettysessions"
 * tables does not exist, it is automatically created.<p>
 *
 * Use of this table is optional.<p>
 *
 * It is a good practice to ensure this table is created in a separate
 * database/schema from the core application tables - and with a different set
 * of user/pass credentials.
 *
 * @see <a href="https://www.eclipse.org/jetty/documentation/current/session-management.html" target="_blank">Jetty session management</a>.
 */
public class DemoSessionHandler {

    private static final Properties PROPS = DemoProperties.INSTANCE.getProps();

    public static SessionHandler create() {
        SessionHandler sessionHandler = new SessionHandler();
        SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
        sessionCache.setSessionDataStore(getJdbcDataStoreFactory()
                .getSessionDataStore(sessionHandler));
        sessionHandler.setSessionCache(sessionCache);
        sessionHandler.setHttpOnly(true);

        // Make additional changes to the SessionHandler here:
        sessionHandler.setMaxInactiveInterval(90 * 60); // seconds

        return sessionHandler;
    }

    private static JDBCSessionDataStoreFactory getJdbcDataStoreFactory() {
        // use this as a property name suffix for db-specific properties:
        String dbType = PROPS.getProperty("demo.db.type");

        DatabaseAdaptor databaseAdaptor = new DatabaseAdaptor();
        String driver = PROPS.getProperty(String.format("%s.%s", "demo.db.driver", dbType));
        databaseAdaptor.setDriverInfo(driver, buildURL(dbType));

        JDBCSessionDataStoreFactory jdbcSessionDataStoreFactory = new JDBCSessionDataStoreFactory();
        jdbcSessionDataStoreFactory.setDatabaseAdaptor(databaseAdaptor);
        jdbcSessionDataStoreFactory.setGracePeriodSec(2 * 60); //seconds
        return jdbcSessionDataStoreFactory;
    }

    private static String buildURL(String dbType) {
        String urlTemplate = null;
        switch (dbType) {
            case "mysql":
                urlTemplate = "%s?user=%s&password=%s";
                break;
            case "h2":
                urlTemplate = "%s;USER=%s;PASSWORD=%s";
                break;
        }
        return String.format(urlTemplate,
                // append dbType to the end of the property names:
                PROPS.getProperty(String.format("%s.%s", "demo.sessiondb.url", dbType)),
                PROPS.getProperty(String.format("%s.%s", "demo.sessiondb.user", dbType)),
                encodeValue(PROPS.getProperty(String.format("%s.%s", "demo.sessiondb.pass", dbType))));
    }

    private static String encodeValue(String unencodedValue) {
        String encodedValue = null;
        try {
            encodedValue = URLEncoder.encode(unencodedValue, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException ex) {
            LoggerUtils.LOGGER.error(String.format("Error URL-encoding the following text: [%s]", unencodedValue), ex);
        }
        return encodedValue;
    }

}
