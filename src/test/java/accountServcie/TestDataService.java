package accountServcie;

import dbService.DataService;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Created by Andrey
 */
public class TestDataService implements DataService {

    private Connection connection = null;
    public Connection getConnection()
    {
        if (connection != null) {
            return connection;
        } else {
            try{
                DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

                return DriverManager.getConnection(
                        "jdbc:mysql://" +
                                "localhost:" +
                                "3306/" +
                                "test?" +
                                "user=root&" +
                                "password=sqroot");

            } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

