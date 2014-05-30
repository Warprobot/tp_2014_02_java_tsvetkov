package utils.resource;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Andrey
 * 22.05.14.
 */
public class ResourceFactoryTest {

    @Test
    public void testGetInstance() throws Exception {
        Assert.assertFalse(ResourceFactory.getInstance() == null);
    }

    @Test
    public void testGet() throws Exception {
        String test = "src/test/testfiles/test.xml";
        TestRes tr =(TestRes) ResourceFactory.getInstance().get(test);
        Assert.assertTrue(tr.getStr().equals("str"));
        Assert.assertTrue(tr.getInteger() == 1);
        Assert.assertTrue(tr.getFloat() == 0.0);
    }
}
