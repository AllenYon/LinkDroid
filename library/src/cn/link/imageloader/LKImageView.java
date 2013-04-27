package cn.link.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageButton;
import cn.link.core.SimpleAsyncTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LKImageView extends ImageButton {
    public static ImageLoaderConfiguration sConfiguration;
    private DisplayImageOptions mDisplayOptions;

    public LKImageView(Context context) {
        super(context);

    }

    public LKImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setDisplayOptions(DisplayImageOptions option) {
        this.mDisplayOptions = option;
    }

    public void display() {
        Bitmap bitmap = null;
        if (mDisplayOptions.isDispalyIfInMemory()) {
            bitmap = sConfiguration.memoryCache.get(mDisplayOptions.getDisplayUrl());
            mDisplayOptions.getDisplayer().display(bitmap, this);
        } else {
            new ImageDisplayTask().execute();
        }
    }

    private class ImageDisplayTask extends SimpleAsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setImageResource(mDisplayOptions.getResetImage());
        }

        /**
         * 1.读取内存缓存
         * 2.读取文件缓存
         * 3.网络/SD/Content 读取图片数据
         * <p/>
         * 图片数据流解码并进行文件缓存
         * 最后内存缓存
         *
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = sConfiguration.memoryCache.get(mDisplayOptions.getDisplayUrl());
            if (bitmap == null) {
                try {
                    bitmap = sConfiguration.discCache.read(mDisplayOptions.getDisplayUrl(),
                            mDisplayOptions, sConfiguration.mDecoder);
                    if (bitmap == null) {
                        InputStream inputStream = sConfiguration.downloader
                                .getStream(mDisplayOptions.getDisplayUrl(), null);
                        bitmap = sConfiguration.discCache.decodeAndWrite(inputStream,
                                mDisplayOptions, sConfiguration.mDecoder);
                        if (bitmap != null) {
                            sConfiguration.memoryCache.put(mDisplayOptions.getDisplayUrl(),
                                    bitmap);
                            return bitmap;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;  //ToDo
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                mDisplayOptions.getDisplayer().display(bitmap, LKImageView.this);
            }
        }
    }

}
