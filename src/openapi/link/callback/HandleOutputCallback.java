package openapi.link.callback;

/**
 * Author: weiqiang
 * Time: 2020/3/28 下午10:34
 */
public abstract class HandleOutputCallback implements Runnable {
    private String str = null;

    protected String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public final void run() {
        canProviderInput();
    }

    protected abstract void canProviderInput();
}