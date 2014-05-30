package utils.vfs;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andrey
 * 24.04.14.
 */
public class VFS implements VirtualFileSystem {

    private static final String delim = File.separator;
    private String root;

    public VFS(String rootPath) {
        root = rootPath;
    }

    @Override
    public boolean check(String path) {
        return (new File(getFullPath(path))).exists();
    }

    @Override
    public boolean isDirectory(String path) {
        return new File(path).isDirectory();
    }

    @Override
    public String getAbsolutePath(String file) {
        return (new File(getFullPath(file))).getAbsolutePath();
    }

    @Override
    public byte[] getBytes(String file)
    {
        DataInputStream ds = getInputStream(file);
        if(ds == null) {
            return null;
        }

        byte[] data = new byte[0];
        try {
            data = new byte[ds.available()];
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ds.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    @Override
    public String getUFT8Text(String file)
    {
        DataInputStream ds = getInputStream(file);
        try {
            InputStreamReader isr = new InputStreamReader(ds, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            while (true) {
                String line = br.readLine();
                if(line == null)
                    break;
                sb.append(line);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterator<String> getIterator(String startDir) {
        return new VFSIterator(startDir);
    }

    private String getFullPath(String path)
    {
        String[] folders = path.split("/");
        StringBuilder pathBuilder = new StringBuilder(root);

        for (int i = 0; i < folders.length; i++) {
            if (!folders[i].isEmpty()) {
                pathBuilder.append(delim).append(folders[i]);
            }
        }
        return pathBuilder.toString();
    }

    private DataInputStream getInputStream(String file)
    {
        FileInputStream fs = null;
        if(check(file)) {
            try {
                fs = new FileInputStream(getAbsolutePath(file));
            } catch (FileNotFoundException IGNORE) {}
        } else {
            return null;
        }
        return new DataInputStream(fs);
    }

    private class VFSIterator implements Iterator<String>{

        private Queue<File> files = new LinkedList<>();

        public VFSIterator(String path){
            files.add(new File(path));
        }

        @Override
        public boolean hasNext() {
            return !files.isEmpty();
        }

        @Override
        public String next() {
            File file = files.peek();
            if(file.isDirectory()){
                Collections.addAll(files, file.listFiles());
            }
            return files.poll().getPath();
        }

        @Override
        public void remove() {
            files.poll();
        }
    }
}
