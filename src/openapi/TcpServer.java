package openapi;

import openapi.link.Context;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class TcpServer {
    private ServerListener serverListener;

    public boolean start(int port) {
        try {
            Selector selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器信息：" + serverSocketChannel.getLocalAddress());
            serverListener = new ServerListener(selector);
            serverListener.start();
        } catch (IOException e) {
            System.out.println("端口号被占用");
            return false;
        }
        return true;
    }

    public void stop() {
        if (serverListener != null) {
            serverListener.exit();
            serverListener = null;
        }
    }

    @SuppressWarnings("all")
    private class ServerListener extends Thread {
        private Selector selector;
        private boolean done = false;

        private ServerListener(Selector selector) {
            this.selector = selector;
        }

        public void run() {
            super.run();
            System.out.println("服务器准备就绪～");
            Selector selector = this.selector;
            while (!done) {
                try {
                    if (selector.select() == 0) {
                        if (done)
                            break;
                        continue;
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        if (done)
                            break;
                        SelectionKey next = iterator.next();
                        iterator.remove();
                        if (next.isAcceptable()) {
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) next.channel();
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            Context.get().getIoProvider().registerInput(socketChannel);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("服务器已关闭！");
        }

        void exit() {
            done = true;
        }
    }
}
