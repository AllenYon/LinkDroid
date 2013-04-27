package cn.link.cacheloader;


import cn.link.AppContext;

import java.io.Serializable;

/**
 * 实现界面二次加载的接口类
 *
 * @author link
 */
public abstract class ICacheLoader implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected static final int DEFALT_LOAD_COUNT = 20;

    public ICacheLoader() {

    }


    public abstract Object getCurrent(AppContext actx) throws Throwable;

    public abstract Object getNewer(AppContext actx) throws Throwable;

    public abstract Object getOlder(AppContext actx) throws Throwable;

    /**
     * @return 缓存文件名字 唯一索引
     */
    public abstract String getFileName(AppContext actx);


    public abstract boolean writeToDisk();

    /**
     * 是否需要对network下载的数据进行缓存 默认为 只有网络刷新的数据才进行缓存
     * <p/>
     * For example : actx.writeObject(object,getFileName());
     *
     * @return default true
     */
    public void onWriteToDisk(AppContext actx, Object object, int source) {
        if (source == CacheLoader.NETWORK) {
            actx.writeObject(getFileName(actx), (Serializable) object);
        }
    }

    public abstract boolean setToMemory();

    /**
     * 从文件或Network 获得数据后，是否保存至内存 for example :
     * <p/>
     * actx.setObject(object,getFileName());
     *
     * @param object
     */
    public void onSetToMemory(AppContext actx, Object object, int source) {
        actx.setObject(getFileName(actx), object);
    }

    /**
     * 若不需要从内存中获得缓存对象 返回null
     * <p/>
     * actx.getObject(getFileName());
     *
     * @return 内存缓存
     * @throws Exception
     */
    public Object getCacheFromMemory(AppContext actx) {
        return actx.getObject(getFileName(actx));
    }

    /**
     * actx.readObject(getFileName());
     *
     * @param actx
     * @return
     */
    public Object getCacheFromDisk(AppContext actx) {
        return actx.readObject(getFileName(actx));
    }

    private int mAddDataCount;

    public int getAddedDataCount() {
        return mAddDataCount;
    }

    public void setAddedDataCount(int count) {
        this.mAddDataCount = count;
    }

}
