package PageGenerator;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Andrey on
 * 13.03.14.
 */
public class PageGeneratorTest {

    @Test
    public void testUserIdPageSuccess() throws Exception
    {
        Random rnd = new Random();

        Integer userId = rnd.nextInt(999);
        Integer refresh = 1 + rnd.nextInt(1000);

        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        String dateStr =  formatter.format(date);

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("refreshPeriod", refresh);
        pageVariables.put("serverTime", dateStr);
        pageVariables.put("userId", userId);

        String test = PageGenerator.getPage("userid.tml", pageVariables);
        Assert.assertTrue(test.contains("<p>Server time: " + dateStr + "</p>"));
        Assert.assertTrue(test.contains("<p>UserId: " + userId + "</p>"));
        Assert.assertTrue(test.contains("setInterval(function(){refresh()}, " + refresh + " );"));
    }

    @Test
    public void testGetPageFailure() throws Exception {
        String err = PageGenerator.getPage("Just a wrong Name", null);
        Assert.assertEquals(err, "");
    }
}