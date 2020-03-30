package openapi.link.core;

import java.nio.channels.SocketChannel;

/**
 * Author: weiqiang
 * Time: 2020/3/28 上午10:30
 */
public interface IoProvider {
    void registerInput(SocketChannel channel);
}
