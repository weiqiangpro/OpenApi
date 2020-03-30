package openapi.link.callback;

/**
 * Author: weiqiang
 * Time: 2020/3/28 下午10:32
 */
public abstract class HandleInputCallback implements Runnable {
    @Override
    public final void run() {
        canProviderInput();
    }

    protected abstract void canProviderInput();
}
