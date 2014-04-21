package org.jmotor.util.persistence.helper;

import org.jmotor.util.ObjectUtilities;
import org.jmotor.util.persistence.dto.ColumnMeta;
import org.jmotor.util.persistence.dto.DataSources;
import org.jmotor.util.persistence.dto.PropertyMapper;
import org.jmotor.util.persistence.dto.SqlStatement;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Component:
 * Description:
 * Date: 13-5-10
 *
 * @author Andy Ai
 */
public class PersistenceHelper {
    public static Connection getConnection(DataSources dataSources) throws SQLException {
        try {
            Class.forName(dataSources.getDriverClass());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getLocalizedMessage(), e);
        }
        return DriverManager.getConnection(
                dataSources.getConnector(),
                dataSources.getUsername(),
                dataSources.getPassword());
    }

    public static int executeUpdate(Connection connection, SqlExecuteEvent event, String sql, Object... parameters) throws SQLException {
        if (null == event) {
            throw new NullPointerException("Sql execute event can't be empty.");
        }
        return execute_update(connection, event, sql, parameters);
    }

    public static int executeUpdate(Connection connection, String sql, Object... parameters) throws SQLException {
        return execute_update(connection, null, sql, parameters);
    }

    private static int execute_update(Connection connection, SqlExecuteEvent event, String sql, Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParameters(preparedStatement, parameters);
            if (null != event) {
                event.executeBefore();
            }
            return preparedStatement.executeUpdate();
        } finally {
            if (null != event) {
                event.executeAfter();
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }

    }

    public static <T> List<T> executeQuery(Connection connection, QueryCallback<T> callback, SqlExecuteEvent event,
                                           String sql, Object... parameters) throws SQLException {
        if (null == event) {
            throw new NullPointerException("Sql execute event can't be empty.");
        }
        return execute_query(connection, callback, event, sql, parameters);
    }

    public static <T> List<T> executeQuery(Connection connection, QueryCallback<T> callback,
                                           String sql, Object... parameters) throws SQLException {
        return execute_query(connection, callback, null, sql, parameters);
    }

    private static <T> List<T> execute_query(Connection connection, QueryCallback<T> callback, SqlExecuteEvent event,
                                             String sql, Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParameters(preparedStatement, parameters);
            if (null != event) {
                event.executeBefore();
            }
            resultSet = preparedStatement.executeQuery();
            int fetchSize = resultSet.getFetchSize();
            List<T> result = new ArrayList<T>(fetchSize > 5 ? fetchSize : 5);
            while (resultSet.next()) {
                result.add(callback.mappingRow(resultSet));
            }
            return result;
        } finally {
            if (null != event) {
                event.executeAfter();
            }
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (null != preparedStatement) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    public static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        if (null == parameters || parameters.length < 1) {
            return;
        }
        int i = 1;
        for (Object parameter : parameters) {
            if (parameter == null) {
                statement.setNull(i++, Types.VARCHAR);
            } else {
                statement.setObject(i++, parameter);
            }
        }
    }


    public static Long getRows(Connection connection, String sql, Object... parameters) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, parameters);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                long count = 0;
                while (resultSet.next()) {
                    count++;
                }
                return count;
            }
        }
    }


    public static List<String> getColumnNames(Connection connection, String entity) throws SQLException {
        ResultSet resultSet = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            resultSet = metaData.getColumns(null, null, entity, null);
            List<String> columns = new ArrayList<String>(resultSet.getFetchSize());
            while (resultSet.next()) {
                columns.add(resultSet.getString(4));
            }
            return columns;
        } finally {
            if (null != resultSet) {
                resultSet.close();
            }
        }
    }

    public static List<ColumnMeta> getColumns(Connection connection, String entity) throws SQLException {
        ResultSet resultSet = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            List<ColumnMeta> columns = new ArrayList<ColumnMeta>(10);
            resultSet = metaData.getColumns(null, null, entity, null);
            while (resultSet.next()) {
                ColumnMeta columnMeta = new ColumnMeta();
                columnMeta.setName(resultSet.getString(4));
                columnMeta.setType(resultSet.getInt(5));
                columns.add(columnMeta);
            }
            return columns;
        } finally {
            if (null != resultSet) {
                resultSet.close();
            }
        }
    }

    public static Object[] getParameters(Object object, SqlStatement sqlStatement) {
        PropertyMapper propertyMapper = sqlStatement.getPropertyMapper();
        Object[] parameters = new Object[propertyMapper.size()];
        int i = 0;
        for (String property : propertyMapper.keyArray()) {
            parameters[i++] = ObjectUtilities.getPropertyValue(object, property);
        }
        return parameters;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (null != connection && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            //ignore
        }
    }

}
