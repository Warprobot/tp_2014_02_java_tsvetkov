package dbService;

import utils.resource.ConnectionRes;
import utils.resource.ResourceFactory;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Created by Andrey
 */
public class ProductionDataService implements DataService{

    private Connection connection = null;
    private ConnectionRes connectionRes = (ConnectionRes) ResourceFactory.getInstance().get("data/connection.xml");

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
                        connectionRes.getPort() + "/" +
                        connectionRes.getDb_name()+ "?" +
                        "user=" + connectionRes.getUser() +"&" +
                        "password=" + connectionRes.getPassword());

            } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

