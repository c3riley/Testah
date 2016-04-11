package org.testah.util.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testah.TS;
import org.testah.util.database.dto.SqlExecutionDto;

public class PostgresDatabase extends AbstractDatabaseUtil {

	public PostgresDatabase(final String databaseName, final String host, final int port, final String dbUser,
			final String dbPassword) {
		super(databaseName, host, port, dbUser, dbPassword);
	}

	public Connection getConnection() throws SQLException {
		Connection connection = null;
		connection = DriverManager.getConnection(getConnectionString(), getDbUser(), getDbPassword());
		return connection;
	}

	public boolean isDriverLoaded() throws Exception {
		Class.forName("org.postgresql.Driver");
		return true;
	}

	public String getConnectionString() {
		return "jdbc:postgresql://" + getHost() + ":" + getPort() + "/" + getDatabaseName();
	}

	public List<HashMap<String, Object>> execuateSelectSql(final String sql) throws SQLException {
		try (Connection conn = getConnection()) {
			return execuateSelectSql(sql, conn);
		}
	}

	public List<HashMap<String, Object>> execuateSelectSql(final String sql, final Connection conn)
			throws SQLException {
		if (null == sql || !sql.toLowerCase().startsWith("select")) {
			throw new RuntimeException("execuateSelectSql can only use Select sql statement!");
		}
		final PreparedStatement pstmt = conn.prepareStatement(sql);
		final List<HashMap<String, Object>> values = new ArrayList<HashMap<String, Object>>();

		try (final ResultSet rs = pstmt.executeQuery()) {
			final ResultSetMetaData md = rs.getMetaData();
			final int columns = md.getColumnCount();

			while (rs.next()) {
				final HashMap<String, Object> row = new HashMap<String, Object>(columns);
				for (int i = 1; i <= columns; ++i) {
					row.put(md.getColumnName(i), rs.getObject(i));
				}
				values.add(row);
			}
		}
		return values;
	}

	public SqlExecutionDto getSqlPerformance(final String sql) throws SQLException {
		try (Connection conn = getConnection()) {
			return getSqlPerformance(sql, conn);
		}
	}

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
