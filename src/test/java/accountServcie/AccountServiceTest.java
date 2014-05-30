package accountServcie;

import dbService.DataService;
import messageSystem.MessageSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.sql.*;

/**
 * Created by Andrey
 * 17.05.14.
 */
public class AccountServiceTest {
    private static MessageSystem messageSystem = new MessageSystem();
    private static DataService dataService = new TestDataService();
    private static AccountService accountService = new AccountService(dataService, messageSystem);

    private static final String testLogin = "1";
    private static final String testPass = "1";

    private void clearTestBase()
    {
        Connection connection = dataService.getConnection();
        try {
            PreparedStatement clear = connection.prepareStatement("select concat(\'TRUNCATE TABLE \', table_schema" +
                    ",\'.\',TABLE_NAME,\';\') FROM INFORMATION_SCHEMA.TABLES where table_schema = \'test\';");
            ResultSet resultSet = clear.executeQuery();
            connection.setAutoCommit(false);
            PreparedStatement disableForeignCheck = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            disableForeignCheck.executeUpdate();
            while (resultSet.next()){
                PreparedStatement t = connection.prepareStatement(resultSet.getString(1));
                t.executeUpdate();
            }
            PreparedStatement enableForeignCheck = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            enableForeignCheck.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Before
    public void setUp() throws Exception
    {
        clearTestBase();
        Statement stmt = dataService.getConnection().createStatement();
        String setUp = "INSERT INTO users (name, password) VALUES" + "" +
                            "('" + testLogin + "', '" + testPass + "')" +
                            "  ON DUPLICATE KEY UPDATE password='" + testPass + "'";
        stmt.execute(setUp);
        stmt.close();
    }

    @After
    public void endTest() throws Exception
    {
        clearTestBase();
    }

    @Test
    public void testCheckPassword() throws Exception {
        Assert.assertTrue(accountService.checkPassword(testLogin, testPass));
    }

    @Test
    public void testRegisterUser() throws Exception {
        final String testLogin2 = "2";
        final String testPass2 = "2";

        accountService.registerUser(testLogin2, testPass2);
        Assert.assertTrue(accountService.checkPassword(testLogin2, testPass2));
    }
}
