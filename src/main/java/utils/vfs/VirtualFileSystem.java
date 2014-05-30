package utils.vfs;

import java.util.Iterator;

/**
 * Created by Andrey
 * 24.04.14.
 */
public interface VirtualFileSystem {

    public boolean check(String path);
    public boolean isDirectory(String path);
    public String getAbsolutePath(String file);
    public byte[] getBytes(String file);
    public String getUFT8Text(String file);
    public Iterator<String> getIterator(String startDir);
}