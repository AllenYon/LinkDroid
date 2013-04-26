package cn.link.core;

public interface UICallBack<T> {
    public void onCompleted(T... ob);

    public void onFailed(Throwable e);
}