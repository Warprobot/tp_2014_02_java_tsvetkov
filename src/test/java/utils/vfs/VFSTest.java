package utils.vfs;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by Andrey
 * 22.05.14.
 */
public class VFSTest {
    static String root = System.getProperty("user.dir");
    VFS vfs = new VFS(root);

    @Test
    public void testCheck() throws Exception {
        Assert.assertTrue(vfs.check("src/test/testfiles/file"));
        Assert.assertFalse(vfs.check("src/test/testfiles/no_file_here"));
    }

    @Test
    public void testIsDirectory() throws Exception {
        Assert.assertTrue(vfs.isDirectory("src/test/testfiles/folder"));
    }

    @Test
    public void testGetAbsolutePath() throws Exception {
        Assert.assertTrue(vfs.getAbsolutePath("src").equals(
                new File(root + File.separator + "src").getAbsolutePath())
        );
    }
}
