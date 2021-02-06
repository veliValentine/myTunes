package group.demo.dataAccess;

import group.demo.logger.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public abstract class Repository {
    protected Connection connection;
    protected final Logger logger;

    public Repository(Logger logger){
        this.logger = logger;
    }

    protected void openConnectionAndLog() throws Exception {
        connection = DriverManager.getConnection(ConnectionHelper.CONNECTION_URL);
        logger.logToConsole("Connection to database opened");
    }

    protected PreparedStatement prepareQuery(String query) throws Exception {
        return connection.prepareStatement(query);
    }

    protected void closeConnectionAndLog() {
        try {
            connection.close();
            logger.logToConsole("Connection to database closed");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        }
    }
}
