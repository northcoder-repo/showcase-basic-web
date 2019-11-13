package org.northcoder.titlewebdemo.dao;

import java.util.Properties;
import org.northcoder.titlewebdemo.util.DemoProperties;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatements;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * The Data Source object which controls access to the underlying database. An
 * enum is used to ensure the data source is a singleton.<p>
 * Includes a
 * <a href="https://github.com/brettwooldridge/HikariCP" target="_blank">Hikari</a>
 * connection pool for database connections.
 */
public enum JdbiDS {

    INST;

    JdbiDS() {
        // The application's standard data source, wrapped in a hikari connection pool:
        jdbi = Jdbi.create(createConnectionPool());
        jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
        jdbi.setSqlLogger(new DemoSqlLogger());
    }

    private final Jdbi jdbi;
    private String dbType = null;
    private final Properties props = DemoProperties.INSTANCE.getProps();

    /**
     * Sets up the
     * <a href="https://github.com/brettwooldridge/HikariCP" target="_blank">Hikari</a>
     * connection pool for database connections.
     *
     * @return the Hikari connection pool.
     */
    private HikariDataSource createConnectionPool() {
        // use this as a property name suffix for db-specific properties, below.
        dbType = props.getProperty("demo.db.type");

        // These are used directly by the Hikari pool:
        HikariConfig hc = new HikariConfig();
        hc.setJdbcUrl(props.getProperty(String.format("%s.%s", "demo.db.url", dbType)));
        hc.setUsername(props.getProperty(String.format("%s.%s", "demo.db.user", dbType)));
        hc.setPassword(props.getProperty(String.format("%s.%s", "demo.db.pass", dbType)));
        // TODO - should be moved to the properties file.
        hc.setMaximumPoolSize(5);  // default is 10
        hc.setMaxLifetime(1800000); // 30 minutes

        // https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        // http://assets.en.oreilly.com/1/event/21/Connector_J%20Performance%20Gems%20Presentation.pdf
        // These are passed through to the MySQL driver, or whatever driver you are using:
        // TODO - should be moved to the properties file.
        hc.addDataSourceProperty("cachePrepStmts", "true");
        hc.addDataSourceProperty("prepStmtCacheSize", "250");
        hc.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hc.addDataSourceProperty("useServerPrepStmts", "true");
        hc.addDataSourceProperty("useLocalSessionState", "true");
        hc.addDataSourceProperty("rewriteBatchedStatements", "true");
        hc.addDataSourceProperty("cacheResultSetMetadata", "true");
        hc.addDataSourceProperty("cacheServerConfiguration", "true");
        hc.addDataSourceProperty("elideSetAutoCommits", "true");
        hc.addDataSourceProperty("maintainTimeStats", "false");

        return new HikariDataSource(hc);
    }

    public Jdbi getDS() {
        return jdbi;
    }

}
