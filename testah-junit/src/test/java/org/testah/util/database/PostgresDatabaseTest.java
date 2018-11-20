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

public class PostgresDatabaseTest {

    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    ResultSetMetaData resultSetMetaData;
    PostgresDatabase postgresDatabase;

    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        resultSetMetaData = mock(ResultSetMetaData.class);
        postgresDatabase = spy(new PostgresDatabase(
            "dbTest", "hostTest", 4454, "userTest", "userPwd"));
    }


    @Test
    public void isDriverLoadedTest() throws Exception {
        assertThat(postgresDatabase.isDriverLoaded(), is(true));
    }

    @Test
    public void getConnectionStringTest() throws Exception {
        assertThat(postgresDatabase.getConnectionString(),
            equalTo("jdbc:postgresql://hostTest:4454/dbTest"));
    }

    @Test(expected = SQLException.class)
    public void getConnectionTestNegative() throws Exception {
        assertThat(postgresDatabase.getConnection(),
            instanceOf(Connection.class));
    }

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

        SqlExecutionDto actual = postgresDatabase.
            getSqlPerformance(testSql, connection);
        assertThat(actual.getDuration(), greaterThanOrEqualTo(1000L));
        assertThat(actual.getSql(), equalTo(testSql));
    }

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
        SqlExecutionDto actual = postgresDatabase.
            getSqlPerformance(testSql);
        assertThat(actual.getDuration(), greaterThanOrEqualTo(1000L));
        assertThat(actual.getSql(), equalTo(testSql));
    }

    @Test
    public void executeSelectSol() throws SQLException {
        final String testSql = "SELECT TEST FROM TEST_TABLE";
        List<String> columns = getColumns(5);
        LinkedHashMap<Integer, List<String>> rows = getRows(columns, 10);
        setupExecuteSelectSol(columns, rows, testSql);

        List<HashMap<String, Object>> actual = postgresDatabase.
            executeSelectSol(testSql, connection);

        assertValues(rows, actual);
    }

    @Test
    public void executeSelectSolPassConn() throws SQLException {
        final String testSql = "SELECT TEST FROM TEST_TABLE";
        List<String> columns = getColumns(5);
        LinkedHashMap<Integer, List<String>> rows = getRows(columns, 10);
        setupExecuteSelectSol(columns, rows, testSql);

        doReturn(connection).when(postgresDatabase).getConnection();
        List<HashMap<String, Object>> actual = postgresDatabase.
            executeSelectSol(testSql);

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
        for (int i = 1; i < numberOfColumns; i++) {
            cols.add("columnName_" + i);
        }
        return cols;
    }

    private LinkedHashMap<Integer, List<String>> getRows(List<String> columns, int rowCount) {
        LinkedHashMap<Integer, List<String>> rows = new LinkedHashMap<>();
        for (int i = 1; i < rowCount; i++) {
            List<String> row = new ArrayList<>();
            rows.put(i, row);
            columns.forEach(col -> {
                row.add(col + "_value_" + (rows.size()));
            });
        }
        return rows;
    }

    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION")
    public void setupExecuteSelectSol(List<String> columns,
                                      LinkedHashMap<Integer, List<String>> rows,
                                      String testSql) throws SQLException {

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