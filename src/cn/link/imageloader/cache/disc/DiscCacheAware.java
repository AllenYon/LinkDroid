package cn.link.imageloader.cache.disc;

import android.graphics.Bitmap;
import cn.link.imageloader.DisplayImageOptions;

import java.io.IOException;
import java.io.InputStream;

public interface DiscCacheAware {


    Bitmap read(String key,DisplayImageOptions options);

    Bitmap decodeAndWrite(InputStream input,DisplayImageOptions options) throws IOException;

    void clear();
}
