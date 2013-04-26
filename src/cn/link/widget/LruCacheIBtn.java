package cn.link.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageButton;
import cn.link.core.SimpleAsyncTask;
import cn.link.core.UICallBack;
import cn.link.utils.Constants;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class LruCacheIBtn extends ImageButton {
    private static final int MAX_MEM_CACHE_SIZE = 1024 * 1024 * 4;
    private static final boolean DEBUG = true;

    private String mUrl;
    private Animation mDisplayAnima;


    public LruCacheIBtn(Context context) {
        super(context);

    }

    public LruCacheIBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 自定 截图方式
    }

    static {
        // 先判断目录是否存在
        File dir = new File(Constants.CACHE_PATH);
        if (!dir.exists()) { // 不存在则创建
            dir.mkdirs();
        }
    }

    public static LruCache<String, Bitmap> mBitmapCache = new LruCache<String, Bitmap>(MAX_MEM_CACHE_SIZE) {
        protected int sizeOf(String key, Bitmap value) {
            int intValue = value.getRowBytes() * value.getHeight();
            return intValue;
        }

        @Override
        protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
            // TODO Auto-generated method stub
            super.entryRemoved(evicted, key, oldValue, newValue);
            if (evicted && oldValue != null && !oldValue.isRecycled()) {
                // HBLog.i("1 btimap entryRemoved " + oldValue.getRowBytes());
                oldValue = null;
            }

        }
    };


    public static void clearAllCache() {
        if (DEBUG) {
            Log.i("", "evictAll bitmap");
        }
        mBitmapCache.evictAll();
    }

    public void recycle() {
        BitmapDrawable bd = (BitmapDrawable) getDrawable();
        if (bd != null) {
            Bitmap bitmap = bd.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap = null;
            } else {
            }
        }
    }


    public void displayWithMemory() {
        displayWithMemory(-1, null, null);
    }

    public void displayWithMemory(Animation anim) {
        displayWithMemory(-1, anim, null);
    }

    public void displayWithMemory(int width) {
        displayWithMemory(width, null, null);
    }

    public void displayWithMemory(int width, UICallBack<Bitmap> callback) {
        displayWithMemory(width, null, callback);
    }

    public void displayWithMemory(int width, Animation anim, UICallBack<Bitmap> callback) {
        Bitmap mBmp = mBitmapCache.get(getUrl());
        if (mBmp == null) {
            display(width, anim, null);
        } else {
            setImageBitmap(mBmp);
            if (anim != null) {
                startAnimation(anim);
            }
        }
    }

    public boolean handleDisplayWithMemory() {
        Bitmap mBmp = mBitmapCache.get(getUrl());
        if (mBmp == null) {
            return true;
        } else {
            setImageBitmap(mBmp);
            return false;
        }
    }

    private void display(int itemWidth, Animation anim, UICallBack<Bitmap> callback) {
        new HBImageLoadTask(itemWidth, anim, callback).execute(mUrl);
    }

    private class HBImageLoadTask extends SimpleAsyncTask<String, Integer, Bitmap> {
        int mItemWidth;
        Animation mAnim;
        UICallBack<Bitmap> mCallback;

        public HBImageLoadTask(int itemWidth, Animation anim, UICallBack<Bitmap> callback) {
            this.mItemWidth = itemWidth;
            this.mAnim = anim;
            this.mCallback = callback;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            String mUrl = params[0];
            Bitmap mBmp = mBitmapCache.get(mUrl);
            if (mBmp == null) {
                mBmp = getBitmapFromDisk(mUrl, mItemWidth);
                if (mBmp == null) {
                    InputStream input = null;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    try {
                        URL url = new URL(mUrl);
                        input = url.openStream();

                        File image = new File(Constants.CACHE_PATH, String.valueOf(mUrl.hashCode()));
                        if (!image.exists()) { // 不存在则保存
                            image.createNewFile();
                            OutputStream fout = null;
                            try {
                                fout = new BufferedOutputStream(new FileOutputStream(image));
                                byte[] buf1 = new byte[4098];
                                int len1 = 0;
                                while ((len1 = input.read(buf1)) != -1) {
                                    fout.write(buf1, 0, len1);
                                    output.write(buf1, 0, len1);
                                }
                                fout.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                fout.close();
                            }
                        }

                        // 未指定 图片宽度 则直接解码
                        if (mItemWidth == -1) {
                            mBmp = BitmapFactory.decodeByteArray(output.toByteArray(), 0, output.size());
                        } else {
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inJustDecodeBounds = true;

                            mBmp = BitmapFactory.decodeByteArray(output.toByteArray(), 0, output.size(), o);

                            if (mItemWidth != -1) {
                                o.inSampleSize = o.outWidth / mItemWidth;
                            }
                            o.inPreferredConfig = Config.ARGB_8888;
                            o.inJustDecodeBounds = false;

                            mBmp = BitmapFactory.decodeByteArray(output.toByteArray(), 0, output.size(), o);

                        }

                        if (mUrl != null && mBmp != null) {
                            mBitmapCache.put(mUrl, mBmp);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            output.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        try {
                            if (input != null) {
                                input.close();
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                } else {
                    mBitmapCache.put(mUrl, mBmp);
                }
            }
            return mBmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                if (DEBUG) {
                }
                setImageBitmap(result);
                if (mAnim != null) {
                    startAnimation(mAnim);
                }
                if (mCallback != null) {
                    mCallback.onCompleted(result);
                }
            }
        }
    }

    @SuppressLint("NewApi")
    protected static void putBitmapToDisk(String strUrl, Bitmap bitmap) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)) {
            return;
        }
        if (bitmap == null) {
            return;
        }
        // 判断文件是否存在
        File image = new File(Constants.CACHE_PATH, String.valueOf(strUrl.hashCode()));
        if (!image.exists()) { // 不存在则保存
            try {
                image.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(image);
                CompressFormat formart = null;
                if (android.os.Build.VERSION.SDK_INT >= 14) {
                    formart = CompressFormat.WEBP;
                } else {
                    formart = CompressFormat.PNG;
                }
                if (bitmap.compress(formart, 100, fileOutputStream)) {
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getBitmapFromDisk(String strUrl, int width) {
        if (strUrl == null || Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)) {
            return null;
        }
        Bitmap mBmp = null;
        File cacheFile = new File(Constants.CACHE_PATH, String.valueOf(strUrl.hashCode()));
        if (cacheFile.exists()) {
            InputStream fileInputStream = null;
            try {
                fileInputStream = new BufferedInputStream(new FileInputStream(cacheFile));
                if (width == -1) {
                    mBmp = BitmapFactory.decodeStream(fileInputStream);
                } else {
                    ByteArrayOutputStream output = null;
                    try {
                        output = new ByteArrayOutputStream();
                        byte[] buf = new byte[4096];
                        int len = 0;
                        while ((len = fileInputStream.read(buf)) != -1) {
                            output.write(buf, 0, len);
                        }
                        BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;
                        mBmp = BitmapFactory.decodeByteArray(output.toByteArray(), 0, output.size(), o);
                        o.inSampleSize = o.outWidth / width;
                        o.inPreferredConfig = Config.ARGB_8888;
                        o.inJustDecodeBounds = false;
                        mBmp = BitmapFactory.decodeByteArray(output.toByteArray(), 0, output.size(), o);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        output.close();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return mBmp;
    }

    // ----------------------Getter_and_Setter------------------------------//

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String strUrl) {
        this.mUrl = strUrl;
    }

    public void setDisplayAnima(Animation anim) {
        mDisplayAnima = anim;
    }

}
