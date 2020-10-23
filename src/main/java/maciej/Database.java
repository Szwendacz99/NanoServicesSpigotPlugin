package maciej;

import java.io.File;
import java.sql.*;

public class Database {

    private final String NAME;
    private Connection connection;
    private final String path;

    public Database(String name, String path) throws SQLException {
        this.NAME = name;
        this.path = path;
        this.connection = openDatabase(path);
        this.connection.setAutoCommit(true);
    }

    private Connection openDatabase(String path) throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + path + File.separator + NAME);
    }

    public PreparedStatement getPreparedStatement(String statement) throws SQLException {
        return connection.prepareStatement(statement);
    }

    public void dropTable(String name) throws SQLException {
        close();
        connection = openDatabase(path);
        PreparedStatement statement = getPreparedStatement("DROP TABLE "+name);
        statement.execute();

    }

    public void close() throws SQLException {
        connection.close();
    }

}
