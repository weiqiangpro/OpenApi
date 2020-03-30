package openapi.link.utils;

/**
 * Author: weiqiang
 * Time: 2020/3/28 下午10:05
 */

public class Constants {
    public static int PORT_SERVER = 8888;
    public static int IOARGS_SIZE = 256;
    public static int OUTPUT_POOL_SIZE_MIN = 16;
    public static int OUTPUT_POOL_SIZE_MAX = 32;

    public static int INPUT_POOL_SIZE_MIN = 32;
    public static int INPUT_POOL_SIZE_MAX = 64;

    public static int IOAEGS_EXCUTOR_SIZE = 1000;


    public static String makeMgs(String mes) {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/json; charset=UTF-8\r\n" +
                "Content-Length: " + mes.getBytes().length + "\r\n\r\n" +
                mes;
    }

}