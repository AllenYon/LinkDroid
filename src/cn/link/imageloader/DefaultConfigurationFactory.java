/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package cn.link.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import cn.link.imageloader.assist.FileNameGenerator;
import cn.link.imageloader.assist.HashCodeFileNameGenerator;
import cn.link.imageloader.assist.StorageUtils;
import cn.link.imageloader.cache.disc.DiscCacheAware;
import cn.link.imageloader.cache.memory.LruCacheImpl;
import cn.link.imageloader.cache.memory.MemoryCacheAware;
import cn.link.imageloader.display.BitmapDisplayer;
import cn.link.imageloader.display.SimpleBitmapDisplayer;
import cn.link.imageloader.download.BaseImageDownloader;
import cn.link.imageloader.download.ImageDownloader;

import java.io.File;

public class DefaultConfigurationFactory {


    public static FileNameGenerator createFileNameGenerator() {
        return new HashCodeFileNameGenerator();
    }

    public static DiscCacheAware createDiscCache(Context context,
                                                 FileNameGenerator discCacheFileNameGenerator,
                                                 int discCacheSize,
                                                 int discCacheFileCount) {
//        if (discCacheSize > 0) {
//            File individualCacheDir = StorageUtils.getIndividualCacheDirectory(context);
//            return new TotalSizeLimitedDiscCache(individualCacheDir, discCacheFileNameGenerator, discCacheSize);
//        } else if (discCacheFileCount > 0) {
//            File individualCacheDir = StorageUtils.getIndividualCacheDirectory(context);
//            return new FileCountLimitedDiscCache(individualCacheDir, discCacheFileNameGenerator, discCacheFileCount);
//        } else {
//            File cacheDir = StorageUtils.getCacheDirectory(context);
//            return new UnlimitedDiscCache(cacheDir, discCacheFileNameGenerator);
//        }
        return null;
    }

    /**
     * Creates reserve disc cache which will be used if primary disc cache becomes unavailable
     */
    public static DiscCacheAware createReserveDiscCache(Context context) {
        File cacheDir = context.getCacheDir();
        File individualDir = new File(cacheDir, "uil-images");
        if (individualDir.exists() || individualDir.mkdir()) {
            cacheDir = individualDir;
        }
        return new TotalSizeLimitedDiscCache(cacheDir, 2 * 1024 * 1024); // limit - 2 Mb
    }

    /**
     * Creates default implementation of {@link MemoryCacheAware} depends on incoming parameters: <br />
     * {@link LruMemoryCache} (for API >= 9) or {@link LRULimitedMemoryCache} (for API < 9).<br />
     * Default cache size = 1/8 of available app memory.
     */
    public static MemoryCacheAware<String, Bitmap> createMemoryCache(int memoryCacheSize) {
        if (memoryCacheSize == 0) {
            memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        }
        MemoryCacheAware<String, Bitmap> memoryCache;
        memoryCache = new LruCacheImpl(memoryCacheSize);
        return memoryCache;
    }

    /**
     * Creates default implementation of {@link ImageDownloader} - {@link BaseImageDownloader}
     */
    public static ImageDownloader createImageDownloader(Context context) {
        return new BaseImageDownloader(context);
    }



    public static BitmapDisplayer createBitmapDisplayer() {
        return new SimpleBitmapDisplayer();
    }

}
