package cn.link.imageloader.cache.disc;

import android.graphics.Bitmap;
import cn.link.imageloader.DisplayImageOptions;
import cn.link.imageloader.decode.ImageDecoder;

import java.io.IOException;
import java.io.InputStream;

public interface DiscCacheAware {


    Bitmap read(String key, DisplayImageOptions options, ImageDecoder decoder) throws IOException;

    Bitmap decodeAndWrite(InputStream input, DisplayImageOptions options, ImageDecoder decoder)
            throws IOException;

    void clear();
}
