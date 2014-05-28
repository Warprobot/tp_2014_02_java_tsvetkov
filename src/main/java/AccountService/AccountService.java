package AccountService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by warprobot on 29.05.14.
 */
public class AccountService {
    private static Map<String, String> accounts = new HashMap<>();

    public static void addUser(String login, String password) {
        accounts.put(login, password);
    }
    public static boolean isRegistered(String login) {
        return accounts.containsKey(login);
    }
    public static boolean checkUser(String login, String password) {
        return accounts.containsKey(login) && password.equals(accounts.get(login));
    }

}
