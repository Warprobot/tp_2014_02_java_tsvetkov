package utils.resource;

/**
 * Created by Andrey
 * 24.04.14.
 */
public class ConnectionRes implements Resource{

    private String port;
    private String db_name;
    private String user;
    private String password;

    public String getPort() {
        return port;
    }

    public String getDb_name() {
        return db_name;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
