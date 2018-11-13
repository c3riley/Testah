package org.testah.util.database;

import org.testah.TS;
import org.testah.util.database.dto.SqlExecutionDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostgresDatabase extends AbstractDatabaseUtil {

    /**
     * Constructor.
     *
     * @param databaseName name of database
     * @param host         host name
     * @param port         database part
     * @param dbUser       db user name
     * @param dbPassword   db password
     */
    public PostgresDatabase(final String databaseName, final String host, final int port, final String dbUser,
                            final String dbPassword) {
        super(databaseName, host, port, dbUser, dbPassword);
    }

    /**
     * Execute SQL query.
     *
     * @param sql the SQL query string
     * @return the SQL response as a map
     * @throws SQLException query execution fails
     */
    public List<HashMap<String, Object>> executeSelectSol(final String sql) throws SQLException {
        try (Connection conn = getConnection()) {
            return executeSelectSol(sql, conn);
        }
    }

    /**
     * Execute SQL query.
     *
     * @param sql  the SQL query string
     * @param conn database connection
     * @return the SQL response as a map
     * @throws SQLException query execution fails
     */
    public List<HashMap<String, Object>> executeSelectSol(final String sql, final Connection conn)
        throws SQLException {
        if (null == sql || !sql.toLowerCase().startsWith("select")) {
            throw new RuntimeException("executeSelectSol can only use Select sql statement!");
        }

        final List<HashMap<String, Object>> values = new ArrayList<>();

        try (final PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (final ResultSet rs = pstmt.executeQuery()) {
                final ResultSetMetaData md = rs.getMetaData();
                final int columns = md.getColumnCount();

                while (rs.next()) {
                    final HashMap<String, Object> row = new HashMap<>(columns);
                    for (int icolumn = 1; icolumn <= columns; ++icolumn) {
                        row.put(md.getColumnName(icolumn), rs.getObject(icolumn));
                    }
                    values.add(row);
                }
            }
        }
        return values;
    }

    /**
     * Get the database connection.
     *
     * @see org.testah.util.database.AbstractDatabaseUtil#getConnection()
     */
    public Connection getConnection() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(getConnectionString(), getDbUser(), getDbPassword());
        return connection;
    }

    /**
     * Check if driver is loaded.
     *
     * @see org.testah.util.database.AbstractDatabaseUtil#isDriverLoaded()
     */
    public boolean isDriverLoaded() throws Exception {
        Class.forName("org.postgresql.Driver");
        return true;
    }

    /* (non-Javadoc)
     * @see org.testah.util.database.AbstractDatabaseUtil#getConnectionString()
     */
    public String getConnectionString() {
        return "jdbc:postgresql://" + getHost() + ":" + getPort() + "/" + getDatabaseName();
    }

    /**
     * Execute SQL query.
     *
     * @param sql the SQL query string
     * @return the SQL response
     * @throws SQLException query execution fails
     */
    public SqlExecutionDto getSqlPerformance(final String sql) throws SQLException {
        try (Connection conn = getConnection()) {
            return getSqlPerformance(sql, conn);
        }
    }

    /**
     * Execute SQL query.
     *
     * @param sql  the SQL query string
     * @param conn database connection
     * @return the SQL response
     * @throws SQLException query execution fails
     */
    public SqlExecutionDto getSqlPerformance(final String sql, final Connection conn) throws SQLException {
        final SqlExecutionDto perf = new SqlExecutionDto(sql);

        try (final Statement stmt = conn.prepareStatement(sql)) {

            perf.start();
            stmt.execute(sql);
            perf.end();

            try {
                final ResultSet rs = stmt.getResultSet();
                if (null != rs) {
                    rs.last();
                    perf.setResultCount(rs.getRow());
                    rs.close();
                }
            } catch (final Exception e) {
                TS.log().warn("Try to get ResultSet row count", e);
            }
            try {
                perf.setUpdateCount(stmt.getUpdateCount());
            } catch (final Exception e) {
                TS.log().warn("Try to get stmt.getUpdateCount()", e);
            }

        }
        return perf;

    }

}
