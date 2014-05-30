package accountServcie;

import dbService.DataService;
import dbService.ProductionDataService;
import dbService.UserDAO;
import dbService.User;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.Subscriber;

import java.sql.SQLException;

/**
 * Created by Andrey
 * 12.03.14.
 */
public class AccountService implements Runnable, Subscriber{

    private final DataService dataService;
    private final MessageSystem messageSystem;
    private final Address address;

    private final static int LATENCY = 0;

    public AccountService(DataService ds, MessageSystem ms)
    {
        dataService = ds;
        messageSystem = ms;
        address = new Address();
        messageSystem.registerService(this);
    }

    public boolean checkPassword(String login, String pass)
    {
        emulateLatency();

        UserDAO userDAO = new UserDAO(dataService);
        try {
            User user = userDAO.get(login);
            return user.getPass().equals(pass);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public boolean registerUser(String login, String pass)
    {
        emulateLatency();

        if ( login.isEmpty() || pass.isEmpty() )
            return false;

        UserDAO userDAO = new UserDAO(dataService);
        try {
            userDAO.create(new User(login, pass));
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    public long getUserId(String login)
    {
        UserDAO userDAO = new UserDAO(dataService);
        long id = -1;
        try {
           id = userDAO.get(login).getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageSystem.execForSubscriber(this);
        }
    }

    private void emulateLatency()
    {
        try {
            Thread.sleep(LATENCY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
