package openapi.link.utils;

import java.io.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: weiqiang
 * Time: 2020/3/28 下午10:28
 */
@SuppressWarnings("all")
public class FileUtils {
    private static ReentrantLock lock = new ReentrantLock();

    private FileUtils() {
    }

    public static String createFile(String fileName) {
        File testFile = new File("./file/", fileName + "_wq");
        try {
            if (!testFile.exists())
                testFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return "wrong";
        }
        return testFile.getAbsolutePath();
    }

    public static String fileInformation(String file) {
        StringBuilder str = new StringBuilder();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("stat " + file, null, null);
            InputStream stderr = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stderr, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) {
                str.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "wrong";
        }
        return str.toString();
    }
}
