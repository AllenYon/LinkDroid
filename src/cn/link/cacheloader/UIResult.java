package cn.link.cacheloader;

import android.content.Context;

public class UIResult<T> {
    private T mObject;
    private int mSourceType;
    private int mTimeType;

    private int mAddedDataCount;

    private Throwable mEx;

    public UIResult() {
        // TODO Auto-generated constructor stub
        this.mSourceType = CacheLoader.NULL;
        this.mTimeType = CacheLoader.START;
    }

    public void showException(Context ctx) {
//		APIException.showException(ctx, mEx);
    }

    public void nextTimeType() {
        // this.mTimeType = mTimeType;
        if (this.mTimeType == CacheLoader.START) {
            this.mTimeType = CacheLoader.FIRST;
            return;
        } else if (this.mTimeType == CacheLoader.FIRST) {
            this.mTimeType = CacheLoader.SECOUND;
            return;
        } else {

        }

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        StringBuffer sb = new StringBuffer();
        if (true) {
            sb.append(mSourceType + "\n").append(mTimeType + "\n");
        }
        return sb.toString();
    }

    public boolean isFromFile() {
        return mSourceType == CacheLoader.FILE;
    }

    public boolean isFromNetwork() {
        return mSourceType == CacheLoader.NETWORK;
    }

    public boolean isFirst() {
        return mTimeType == CacheLoader.FIRST;
    }

    public boolean isSecond() {
        return mTimeType == CacheLoader.SECOUND;
    }

    public int getSourceType() {
        return mSourceType;
    }

    public int getTimeType() {
        return mTimeType;
    }

    public Throwable getEx() {
        return mEx;
    }

    public boolean hasEx() {
        if (mEx != null) {
            return true;
        } else {
            return false;
        }
    }

    public T getObject() {
        return mObject;
    }

    public void setObject(T mObject) {
        this.mObject = mObject;
    }

    public void setSourceType(int mSourceType) {
        this.mSourceType = mSourceType;
    }

    public void setEx(Throwable mEx) {
        this.mEx = mEx;
    }

    public int getAddedDataCount() {
        return mAddedDataCount;
    }

    public void setAddedDataCount(int mAddedDataCount) {
        this.mAddedDataCount = mAddedDataCount;
    }
}
