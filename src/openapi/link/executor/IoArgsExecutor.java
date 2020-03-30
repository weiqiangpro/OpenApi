package openapi.link.executor;

import openapi.link.IoArgs;
import openapi.link.utils.Constants;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Author: weiqiang
 * Time: 2020/3/28 上午8:57
 */
public class IoArgsExecutor {
    private  static  final ConcurrentLinkedQueue<IoArgs>  queue = new ConcurrentLinkedQueue<>();

    private IoArgsExecutor(){}

    public static void setup(){
        for (int i = 0; i < Constants.IOAEGS_EXCUTOR_SIZE; i++) {
            queue.add(new IoArgs());
        }
    }

    public static IoArgs get(){
       return queue.poll();
    }

    public static void  add(IoArgs args){
        queue.add(args);
    }
}
