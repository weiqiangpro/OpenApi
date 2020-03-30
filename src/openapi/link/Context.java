package openapi.link;

import openapi.link.core.IoProvider;

/**
 * Author: weiqiang
 * Time: 2020/3/28 上午10:24
 */
public class Context {
    private static Context INSTANCE;
    private final IoProvider ioProvider;

    private Context(IoProvider ioProvider) {
        this.ioProvider = ioProvider;
    }

    public IoProvider getIoProvider() {
        return ioProvider;
    }

    public static Context get() {
        return INSTANCE;
    }

    public static void setup(IoProvider ioProvider) {
        INSTANCE = new Context(ioProvider);
    }

}
