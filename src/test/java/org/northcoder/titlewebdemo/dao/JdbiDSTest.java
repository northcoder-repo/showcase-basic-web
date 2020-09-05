package org.northcoder.titlewebdemo.dao;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatements;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A singleton for an in-memory h2 test database. The database is created and
 * populated for each execution of the test suite (this is used instead of DB
 * stubs/mocks). The database is destroyed when the application's JVM exits.
 * 
 * This database will not be created if the test suite is not run.
 *
 * DS stands for "data source".
 */
public enum JdbiDSTest {

    INST;

    JdbiDSTest() {
        this.jdbi = Jdbi.create(createConnection());
        jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
        jdbi.setSqlLogger(new DemoSqlLogger());
    }

    private final Jdbi jdbi;

    private Connection createConnection() {
        Connection conn = null;
        String testUrl = String.join("", "jdbc:h2:mem:test;",
                "DB_CLOSE_ON_EXIT=FALSE;",
                "AUTOCOMMIT=ON;",
                "DB_CLOSE_DELAY=-1;",
                "INIT=RUNSCRIPT FROM './h2/test/h2_test_db_setup.sql'");
        String testUser = "sa";
        String testPass = "sapass";

        try {
            conn = DriverManager.getConnection(testUrl, testUser, testPass);
        } catch (SQLException ex) {
            Logger.getLogger(JdbiDSTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public Jdbi getDS() {
        return jdbi;
    }

}
