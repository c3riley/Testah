package org.testah.runner.jdbc.load;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcDriver {

    final private String urlString;
    final private Connection connection;

    public JdbcDriver(String connectionString) throws SQLException {
        this.urlString = connectionString;
        connection = DriverManager.getConnection(connectionString);
    }

    public PreparedStatement getPreparedStatement(String queryString) throws SQLException {
        return getConnection().prepareStatement(queryString);
    }

    public ResultSet executeQuery(String queryString) throws SQLException {
        return getPreparedStatement(queryString).executeQuery();
    }

    public boolean execute(String queryString) throws SQLException {
        return getPreparedStatement(queryString).execute();
    }

    public void close() throws SQLException {
        getConnection().close();
    }

    public String getUrlString() {
        return urlString;
    }

    public Connection getConnection() {
        return connection;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("%nJdbcDriver%nURL = %s", getUrlString()));
        try {
            Properties props = getConnection().getClientInfo();
            if (props != null) {
                sb.append(String.format(",%nProperties = %s", props.toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
