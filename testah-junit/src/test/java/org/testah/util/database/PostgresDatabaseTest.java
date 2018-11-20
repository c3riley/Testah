package org.testah.util.database;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.http.Header;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testah.TS;
import org.testah.driver.http.response.ResponseDto;
import org.testah.util.database.dto.SqlExecutionDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * The type Postgres database test.
 */
public class PostgresDatabaseTest {

    /**
     * The Connection.
     */
    Connection connection;
    /**
     * The Prepared statement.
     */
    PreparedStatement preparedStatement;
    /**
     * The Result set.
     */
    ResultSet resultSet;
    /**
     * The Result set meta data.
     */
    ResultSetMetaData resultSetMetaData;
    /**
     * The Postgres database.
     */
    PostgresDatabase postgresDatabase;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        resultSetMetaData = mock(ResultSetMetaData.class);
        postgresDatabase = spy(new PostgresDatabase(
            "dbTest", "hostTest", 4454, "userTest", "userPwd"));
    }


    /**
     * Is driver loaded test.
     *
     * @throws Exception the exception
     */
    @Test
    public void isDriverLoadedTest() throws Exception {
        assertThat(postgresDatabase.isDriverLoaded(), is(true));
    }

    /**
     * Gets connection string test.
     *
     * @throws Exception the exception
     */
    @Test
    public void getConnectionStringTest() throws Exception {
        assertThat(postgresDatabase.getConnectionString(),
            equalTo("jdbc:postgresql://hostTest:4454/dbTest"));
    }

    /**
     * Gets connection test negative.
     *
     * @throws Exception the exception
     */
    @Test(expected = SQLException.class)
    public void getConnectionTestNegative() throws Exception {
        assertThat(postgresDatabase.getConnection(),
            instanceOf(Connection.class));
    }

    /**
     * Gets sql performance test.
     *
     * @throws SQLException the sql exception
     */
    @Test
    public void getSqlPerformanceTest() throws SQLException {
        final String testSql = "SELECT TEST FROM TEST_TABLE";
        List<String> columns = getColumns(5);
        LinkedHashMap<Integer, List<String>> rows = getRows(columns, 10);
        setupExecuteSelectSol(columns, rows, testSql);


        Answer answerGetObject = new Answer() {
            public Object answer(InvocationOnMock invocation) {
                TS.util().pause(1000L);
                return true;
            }
        };
        doAnswer(answerGetObject).when(preparedStatement).execute(any());

        SqlExecutionDto actual = postgresDatabase.getSqlPerformance(testSql, connection);
        assertThat(actual.getDuration(), greaterThanOrEqualTo(1000L));
        assertThat(actual.getSql(), equalTo(testSql));
    }

    /**
     * Gets sql performance no conn test.
     *
     * @throws SQLException the sql exception
     */
    @Test
    public void getSqlPerformanceNoConnTest() throws SQLException {
        final String testSql = "SELECT TEST FROM TEST_TABLE";
        List<String> columns = getColumns(5);
        LinkedHashMap<Integer, List<String>> rows = getRows(columns, 10);
        setupExecuteSelectSol(columns, rows, testSql);


        Answer answerGetObject = new Answer() {
            public Object answer(InvocationOnMock invocation) {
                TS.util().pause(1000L);
                return true;
            }
        };
        doAnswer(answerGetObject).when(preparedStatement).execute(any());

        doReturn(connection).when(postgresDatabase).getConnection();
        SqlExecutionDto actual = postgresDatabase.getSqlPerformance(testSql);
        assertThat(actual.getDuration(), greaterThanOrEqualTo(1000L));
        assertThat(actual.getSql(), equalTo(testSql));
    }

    /**
     * Execute select sol.
     *
     * @throws SQLException the sql exception
     */
    @Test
    public void executeSelectSol() throws SQLException {
        final String testSql = "SELECT TEST FROM TEST_TABLE";
        List<String> columns = getColumns(5);
        LinkedHashMap<Integer, List<String>> rows = getRows(columns, 10);
        setupExecuteSelectSol(columns, rows, testSql);

        List<HashMap<String, Object>> actual = postgresDatabase.executeSelectSol(testSql, connection);

        assertValues(rows, actual);
    }

    /**
     * Execute select sol pass conn.
     *
     * @throws SQLException the sql exception
     */
    @Test
    public void executeSelectSolPassConn() throws SQLException {
        final String testSql = "SELECT TEST FROM TEST_TABLE";
        List<String> columns = getColumns(5);
        LinkedHashMap<Integer, List<String>> rows = getRows(columns, 10);
        setupExecuteSelectSol(columns, rows, testSql);

        doReturn(connection).when(postgresDatabase).getConnection();
        List<HashMap<String, Object>> actual = postgresDatabase.executeSelectSol(testSql);

        assertValues(rows, actual);

    }

    private void assertValues(LinkedHashMap<Integer, List<String>> expected, List<HashMap<String, Object>> actual) {
        assertThat(actual.size(), equalTo(expected.size()));
        expected.forEach((key, row) -> {
            assertThat(actual.get(key - 1).values().toArray(), equalTo(row.toArray()));
        });
    }

    private List<String> getColumns(int numberOfColumns) {
        List<String> cols = new ArrayList<>();
        for (int ctr = 1; ctr < numberOfColumns; ctr++) {
            cols.add("columnName_" + ctr);
        }
        return cols;
    }

    private LinkedHashMap<Integer, List<String>> getRows(List<String> columns, int rowCount) {
        LinkedHashMap<Integer, List<String>> rows = new LinkedHashMap<>();
        for (int ctr = 1; ctr < rowCount; ctr++) {
            List<String> row = new ArrayList<>();
            rows.put(ctr, row);
            columns.forEach(col -> {
                row.add(col + "_value_" + (rows.size()));
            });
        }
        return rows;
    }

    /**
     * Sets execute select sol.
     *
     * @param columns the columns
     * @param rows    the rows
     * @param testSql the test sql
     * @throws SQLException the sql exception
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION")
    public void setupExecuteSelectSol(List<String> columns,
                                      LinkedHashMap<Integer, List<String>> rows,
                                      String testSql) throws SQLException {

        /**
         * Answer for use with the mock, will return true while there are rows available.
         */
        Answer answerNext = new Answer() {
            private int row = 0;

            public Object answer(InvocationOnMock invocation) {
                if (row++ < rows.size()) {
                    return true;
                }
                return false;
            }
        };
        doAnswer(answerNext).when(resultSet).next();

        Answer answerGetColumnName = new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return columns.get((int) invocation.getArgument(0) - 1);
            }
        };
        doAnswer(answerGetColumnName).when(resultSetMetaData).getColumnName(anyInt());

        Answer answerGetObject = new Answer() {
            private int row = 0;

            public Object answer(InvocationOnMock invocation) {
                int index = (int) invocation.getArgument(0) - 1;
                if (index == 0) {
                    row++;
                }
                return rows.get(row).get(index);
            }

        };
        doAnswer(answerGetObject).when(resultSet).getObject(anyInt());

        when(resultSetMetaData.getColumnCount()).thenReturn(columns.size());
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(testSql)).thenReturn(preparedStatement);
    }


    /**
     * Test getter setters.
     */
    @Test
    public void testGetterSetters() {
        assertThat(postgresDatabase.getHost(),
            equalTo("hostTest"));
        assertThat(postgresDatabase.getPort(),
            equalTo(4454));
        assertThat(postgresDatabase.getDbUser(),
            equalTo("userTest"));
        assertThat(postgresDatabase.getDbPassword(),
            equalTo("userPwd"));
        assertThat(postgresDatabase.getDatabaseName(),
            equalTo("dbTest"));
    }

}