package cn.link.imageloader.display;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-4-26
 * Time: PM2:50
 * To change this template use File | Settings | File Templates.
 */
public interface BitmapProgressListener {
    public void onStart(int max);

    public void onProgress(int progress);

    public void onEnd();


}
