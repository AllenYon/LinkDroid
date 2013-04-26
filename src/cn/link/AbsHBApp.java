package cn.link;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import cn.link.utils.ConfigUtils;

import java.io.*;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * @author Link
 */
public abstract class AbsHBApp extends Application {
    private int mScreenWidth = -1;
    private int mScreenHeight = -1;

    /**
     * SoftRefrence Container
     */
    private HashMap<String, SoftReference<Object>> mAppCache = new HashMap<String, SoftReference<Object>>();

    /**
     * HBEventCallback Container
     */
    private HashMap<String, HBEventCallback> mCallbacks = new HashMap<String, HBEventCallback>();

    public static AbsHBApp getInstance(Context context) {
        return (AbsHBApp) context.getApplicationContext();
    }

    public interface HBEventCallback {
        public void onDo(Bundle bundle);
    }

    public void sendEvent(String target, Bundle bundle) {
        if (mCallbacks.containsKey(target)) {
            HBEventCallback callback = mCallbacks.get(target);
            callback.onDo(bundle);
        }
    }

    public void registerEventCallback(String target, HBEventCallback callback) {
        if (!mCallbacks.containsKey(target)) {
            mCallbacks.put(target, callback);
        }
    }

    public void unRegisterEventCallback(String target) {
        mCallbacks.remove(target);
    }

    /**
     * Set object to Memory
     *
     * @param str
     * @param ob
     */
    public void setObject(String str, Object ob) {
        mAppCache.put(str, new SoftReference<Object>(ob));
    }

    public void remove(String str) {
        mAppCache.remove(str);
    }

    /**
     * Get object from Memory
     *
     * @param str
     * @return
     */
    public Object getObject(String str) {
        if (mAppCache.containsKey(str)) {
            SoftReference<Object> sob = mAppCache.get(str);
            Object ob = sob.get();
            if (ob != null) {
                return ob;
            }
        }
        return null;

    }

    /**
     * @param file
     * @return
     */
    public boolean fileExist(String file) {
        // return this.isFileExist(file);
        boolean result = false;
        String[] listFile = fileList();
        for (String string : listFile) {
            if (string.equals(file)) {
                result = true;
                return result;
            }
        }
        return result;
    }

    /**
     * Write object to Disk
     *
     * @param ser
     * @param file
     * @throws java.io.IOException
     */
    public boolean writeObject(String key, Serializable ser) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput(key, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }

    /**
     * Read an object from the Disk
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public Serializable readObject(String file) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
            }
        }
    }
//    public void deleteObject(String fileName){
//        String[] listFile = fileList();
//        for (String string : listFile) {
//            if (string.equals(fileName)) {
//
//                return result;
//            }
//        }
//
//    }

    /**
     * @return
     */
    public String getAppVersionName() {
        try {
            // ---get the package info---
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            String versionName = pi.versionName;
            if (versionName == null) {
                return "v1.0";
            } else {
                return versionName;
            }
        } catch (Exception e) {
            return "v1.0";
        }
    }

    public String readMetaData(String key) throws PackageManager.NameNotFoundException {
        PackageInfo appInfo = getPackageManager().getPackageInfo(getPackageName(),
                PackageManager.GET_META_DATA);
        String result = appInfo.applicationInfo.metaData.getString(key);
        if (result == null) {
            int iresult = appInfo.applicationInfo.metaData.getInt(key);
            return String.valueOf(iresult);
        }
        return result;

    }


    /**
     * @return
     */
    public boolean isWiFiState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);// 获取系统的连接服务
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();// 获取网络的连接情况
        // 可能为Null
        if (activeNetInfo != null) {
            if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return false;
            }
        }
        return false;
    }

    public boolean isWiFiOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null &&
                networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
                networkInfo.isConnected() &&
                networkInfo.isAvailable());
    }

    /**
     * 获取当前屏幕的宽
     *
     * @param context
     * @return
     */
    public int getScreenWidth(Context context) {
        if (mScreenWidth == -1) {
            DisplayMetrics metric = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
            mScreenWidth = metric.widthPixels;
        }
        return mScreenWidth; // 屏幕宽度（像素）

    }

    /**
     * 获取当前的屏幕的高
     *
     * @param context
     * @return
     */
    public int getScreenHeight(Context context) {
        if (mScreenHeight == -1) {
            DisplayMetrics metric = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
            mScreenHeight = metric.heightPixels;
        }
        return mScreenHeight; // 屏幕宽度（像素）
    }


    /**
     * 标记应用已经登录过了
     */
    public void setAppLoaded() {
        ConfigUtils.writeBoolean(getApplicationContext(), "app_first_load", false);
    }

    public boolean isFirstLoad() {
        if (ConfigUtils.readBoolean(getApplicationContext(), "app_first_load", true)) {
            return true;
        }
        return false;
    }

}
