package openapi.link.core;

import openapi.link.MySocketChannel;
import openapi.link.utils.Constants;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: weiqiang
 * Time: 2020/3/28 上午10:34
 */
public class Provider implements IoProvider {
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final AtomicBoolean inRegIn = new AtomicBoolean(false);

    private final Selector readSelector;

    private final ExecutorService inputHandel;
    private final ExecutorService outputHandel;

    public Provider() throws IOException {
        inputHandel = new ThreadPoolExecutor(Constants.INPUT_POOL_SIZE_MIN
                , Constants.INPUT_POOL_SIZE_MAX
                , 10
                , TimeUnit.SECONDS
                , new LinkedBlockingDeque<>());
        outputHandel = new ThreadPoolExecutor(Constants.OUTPUT_POOL_SIZE_MIN
                , Constants.OUTPUT_POOL_SIZE_MAX
                , 10
                , TimeUnit.SECONDS
                , new LinkedBlockingDeque<>());
        this.readSelector = Selector.open();
        startRead();
    }

    private void startRead() {
        Thread thread = new Thread("Clink IoSelectorProvider ReadSelector Thread") {
            @Override
            public void run() {
                while (!isClosed.get()) {

                    try {
                        if (readSelector.select() == 0) {
                            waitSelection();
                            continue;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Set<SelectionKey> selectionKeys = readSelector.selectedKeys();
                    for (SelectionKey selectionKey : selectionKeys) {
                        if (selectionKey.isReadable()) {
                            selectionKey.cancel();
                            readSelector.wakeup();
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            new MySocketChannel(channel, inputHandel, outputHandel);
                        }
                    }
                    selectionKeys.clear();
                }
            }
        };
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }


    public void registerInput(SocketChannel channel) {
        synchronized (inRegIn) {
            try {
                inRegIn.set(true);
                readSelector.wakeup();
                channel.configureBlocking(false);
                channel.register(readSelector, SelectionKey.OP_READ);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                inRegIn.set(false);
                try {
                    inRegIn.notify();
                } catch (Exception ignored) {
                }
            }
        }
    }


    private void waitSelection() {
        synchronized (inRegIn) {
            try {
                if (inRegIn.get())
                    inRegIn.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
