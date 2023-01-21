package org.northcoder.titlewebdemo.util;

import java.util.Properties;
import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * Configuration of the embedded Jetty server is controlled here, including
 * activation of HTTPS and HTTP/2.
 *
 * -------------------------------------------------------------------------------------------
 *
 * WARNING - this is out-of-date. See https://northcoder.com/post/jetty-11-secure-connections/
 *
 * -------------------------------------------------------------------------------------------
 *
 */
public class DemoJetty {

    public static Server create() {
        Properties props = DemoProperties.INSTANCE.getProps();

        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(Integer.parseInt(props.getProperty("demo.http.port")));
        server.addConnector(connector);

        // HTTP Configuration
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        httpConfig.setSecureScheme("https");
        int httpsPort = Integer.parseInt(props.getProperty("demo.https.port"));
        httpConfig.setSecurePort(httpsPort);

        // SSL Context Factory for HTTPS and HTTP/2
        SslContextFactory sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(props.getProperty("demo.keystore.path"));
        sslContextFactory.setKeyStorePassword(props.getProperty("demo.keystore.pass"));
        sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

        // HTTPS Configuration
        HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        // HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(httpsConfig);
        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol("h2"); // not to be confused with H2 DB.

        // SSL Connection Factory
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory,
                alpn.getProtocol());

        // HTTP/2 Connector
        ServerConnector http2Connector = new ServerConnector(server, ssl, alpn, h2,
                new HttpConnectionFactory(httpsConfig));
        http2Connector.setPort(httpsPort);
        server.addConnector(http2Connector);
        ALPN.debug = Boolean.parseBoolean(props.getProperty("demo.alpn.debug"));

        return server;
    }
}
