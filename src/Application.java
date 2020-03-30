import openapi.TcpServer;
import openapi.link.Context;
import openapi.link.core.Provider;
import openapi.link.executor.IoArgsExecutor;
import openapi.link.utils.Constants;

import java.io.File;
import java.io.IOException;

/**
 * Author: weiqiang
 * Time: 2020/3/26 下午11:22
 */
public class Application {
    @SuppressWarnings("all")
    public static void main(String[] args) {
        try {
            File file = new File("file");
            if (!file.exists()) {
                file.mkdirs();
            }

            IoArgsExecutor.setup();
            Context.setup(new Provider());
            TcpServer tcpServer = new TcpServer();
            boolean start = tcpServer.start(Constants.PORT_SERVER);
            if (!start) {
                System.out.println("TCP服务器启动失败");
                return;
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }

    }
}
