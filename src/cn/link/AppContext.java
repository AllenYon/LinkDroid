package cn.link;

import android.content.Context;
import cn.link.cacheloader.CacheLoader;
import com.squareup.otto.Bus;

import java.util.LinkedHashMap;


/**
 * 应用程序上下文
 *
 * @author Link
 */
public class AppContext extends AbsHBApp {
    private CacheLoader mCacheLoader;
    private Bus mBus = new Bus();

    public static AppContext getInstance(Context context) {
        return (AppContext) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mCacheLoader = new CacheLoader(this);

    }

    public Bus getBus() {
        return mBus;
    }


    public CacheLoader getCacheLoader() {
        return mCacheLoader;
    }


}
