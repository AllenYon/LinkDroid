package cn.link.imageloader.cache.disc;

import android.graphics.Bitmap;
import cn.link.imageloader.decode.ImageDecodingInfo;

import java.io.File;

public interface DiscCacheAware {

    void put(String key, Bitmap bitmap);

    Bitmap get(String key);

    Bitmap decode(String url,ImageDecodingInfo info);


    void clear();
}
