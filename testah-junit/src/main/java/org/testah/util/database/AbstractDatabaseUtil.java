package org.testah.util.database;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDatabaseUtil {

    private final String databaseName;
    private final String host;
    private final int port;
    private final String dbUser;
    private final String dbPassword;

    /**
     * Constructor.
     * @param databaseName name of database
     * @param host database host name
     * @param port database port
     * @param dbUser database user name
     * @param dbPassword database password
     */
    public AbstractDatabaseUtil(final String databaseName, final String host, final int port, final String dbUser,
                                final String dbPassword)
    {
        this.databaseName = databaseName;
        this.host = host;
        this.port = port;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public abstract Connection getConnection() throws SQLException;

    public abstract boolean isDriverLoaded() throws Exception;

    public abstract String getConnectionString() throws Exception;

    protected String getDbUser() {
        return dbUser;
    }

    protected String getDbPassword() {
        return dbPassword;
    }

    protected String getHost() {
        return host;
    }

    protected int getPort() {
        return port;
    }

    protected String getDatabaseName() {
        return databaseName;
    }

}
