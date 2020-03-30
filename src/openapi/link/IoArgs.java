package openapi.link;

import openapi.link.utils.Constants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Author: weiqiang
 * Time: 2020/3/28 上午10:26
 */
public class IoArgs {
    private byte[] bytes = new byte[Constants.IOARGS_SIZE];
    private ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    private String str = "";

    public int read(SocketChannel channel) {
        str = "";
        try {
            while (true) {
                byteBuffer.clear();
                int read = 0;
                read = channel.read(byteBuffer);
                if (read <= 0)
                    break;
                str += new String(bytes, 0, read);
            }
        } catch (IOException e) {
            return -1;
        }
        return 1;
    }

    public void write(SocketChannel channel, String str) throws IOException {
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        int offset = 0;
        int size = 0;
        int send = 0;
        byteBuffer.clear();
        while (true) {
            size = Math.min(length - offset, byteBuffer.remaining());
            if (size == 0)
                break;
            byteBuffer.limit(size);
            byteBuffer.put(bytes, offset, size);
            byteBuffer.flip();
            channel.write(byteBuffer);
            if (size + offset >= length)
                break;
            offset += size;
            byteBuffer.clear();
        }

    }

    public String fileName() {
        int finish1 = str.indexOf("HTTP") - 1;
        int finish2 = str.indexOf("?") - 1;

        if (finish2 != -2 && finish2 < finish1)
            finish1 = finish2;
        int start = str.indexOf("/");
        if (start == -1 || finish1 == -2 || finish1 <= start) {
            return "null";
        }
        return str.substring(start, finish1);
    }
}
