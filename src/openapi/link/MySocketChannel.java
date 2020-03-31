package openapi.link;

import openapi.link.callback.HandleInputCallback;
import openapi.link.callback.HandleOutputCallback;
import openapi.link.executor.IoArgsExecutor;
import openapi.link.utils.CloseUtil;
import openapi.link.utils.Constants;
import openapi.link.utils.FileUtils;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

/**
 * Author: weiqiang
 * Time: 2020/3/28 上午10:50
 */
public class MySocketChannel implements Closeable {
    private Logger log = Logger.getLogger("a");
    private boolean isClosed = false;
    private final SocketChannel channel;
    private IoArgs ioArgs;
    private ExecutorService outputHandel;
    private boolean ifGet = false;

    public MySocketChannel(SocketChannel channel, ExecutorService inputHandel, ExecutorService outputHandel) {
        IoArgs args = IoArgsExecutor.get();
        if (args == null) {
            this.ioArgs = new IoArgs();
            log.info("null");
        }
        else {
            this.ioArgs = args;
            ifGet = true;
        }
        this.channel = channel;
        this.outputHandel = outputHandel;
        HandleInputCallback inputCallback = new HandleInputCallback() {
            @Override
            protected void canProviderInput() {
                if (isClosed) {
                    return;
                }
                IoArgs args = MySocketChannel.this.ioArgs;
                int read = args.read(channel);
                if (read <= 0) {
                    startSend(FileUtils.createFile("wrong"));
                    return;
                }
                startSend(FileUtils.createFile(args.fileName()));
            }
        };
        inputHandel.execute(inputCallback);
    }

    @Override
    public void close() throws IOException {
        isClosed = true;
        if (ifGet)
            IoArgsExecutor.add(this.ioArgs);
        this.ioArgs = null;
        channel.close();
    }

    private void startSend(String file) {
        outputCallback.setStr(file);
        outputHandel.execute(outputCallback);
//        outputCallback.run();
    }

    private final HandleOutputCallback outputCallback = new HandleOutputCallback() {
        @Override
        protected void canProviderInput() {
            if (isClosed) {
                return;
            }
            IoArgs args = MySocketChannel.this.ioArgs;
            String msg = FileUtils.fileInformation(getStr());
            try {
                args.write(channel, Constants.makeMgs(msg));
            } catch (IOException ignored) {
            } finally {
                CloseUtil.close(MySocketChannel.this);

            }
        }
    };


}