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
import android.graphics.Bitmap.CompressFormat;
import cn.link.imageloader.assist.FileNameGenerator;
import cn.link.imageloader.assist.L;
import cn.link.imageloader.cache.disc.DiscCacheAware;
import cn.link.imageloader.cache.memory.LruCacheImpl;
import cn.link.imageloader.cache.memory.MemoryCacheAware;
import cn.link.imageloader.decode.ImageDecoder;
import cn.link.imageloader.download.ImageDownloader;

import java.util.concurrent.Executor;

public final class ImageLoaderConfiguration {

    final Context context;

    final CompressFormat imageCompressFormatForDiscCache;
    final int imageQualityForDiscCache;

    final MemoryCacheAware<String, Bitmap> memoryCache;
    final DiscCacheAware discCache;
    final ImageDownloader downloader;
    final ImageDecoder decoder;
    final DisplayImageOptions defaultDisplayImageOptions;
    final boolean loggingEnabled;

    final DiscCacheAware reserveDiscCache;

    private ImageLoaderConfiguration(final Builder builder) {
        context = builder.context;
        imageCompressFormatForDiscCache = builder.imageCompressFormatForDiscCache;
        imageQualityForDiscCache = builder.imageQualityForDiscCache;
        discCache = builder.discCache;
        memoryCache = builder.memoryCache;
        defaultDisplayImageOptions = builder.defaultDisplayImageOptions;
        loggingEnabled = builder.loggingEnabled;
        downloader = builder.downloader;
        decoder = builder.decoder;

        reserveDiscCache = DefaultConfigurationFactory.createReserveDiscCache(context);
    }

    public static ImageLoaderConfiguration createDefault(Context context) {
        return new Builder(context).build();
    }

    public static class Builder {

        private static final String WARNING_OVERLAP_DISC_CACHE_PARAMS = "discCache(), discCacheSize() and discCacheFileCount calls overlap each other";
        private static final String WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR = "discCache() and discCacheFileNameGenerator() calls overlap each other";
        private static final String WARNING_OVERLAP_MEMORY_CACHE = "memoryCache() and memoryCacheSize() calls overlap each other";
        private static final String WARNING_OVERLAP_EXECUTOR = "threadPoolSize(), threadPriority() and tasksProcessingOrder() calls "
                + "can overlap taskExecutor() and taskExecutorForCachedImages() calls.";


        private Context context;

        private CompressFormat imageCompressFormatForDiscCache = null;
        private int imageQualityForDiscCache = 0;


        private boolean denyCacheImageMultipleSizesInMemory = false;

        private int memoryCacheSize = 0;
        private int discCacheSize = 0;
        private int discCacheFileCount = 0;

        private MemoryCacheAware<String, Bitmap> memoryCache = null;
        private DiscCacheAware discCache = null;
        private FileNameGenerator discCacheFileNameGenerator = null;
        private ImageDownloader downloader = null;
        private ImageDecoder decoder;
        private DisplayImageOptions defaultDisplayImageOptions = null;

        private boolean loggingEnabled = false;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }


        public Builder denyCacheImageMultipleSizesInMemory() {
            this.denyCacheImageMultipleSizesInMemory = true;
            return this;
        }


        public Builder memoryCacheSize(int memoryCacheSize) {
            if (memoryCacheSize <= 0)
                throw new IllegalArgumentException("memoryCacheSize must be a positive number");

            if (memoryCache != null) {
                L.w(WARNING_OVERLAP_MEMORY_CACHE);
            }

            this.memoryCacheSize = memoryCacheSize;
            return this;
        }

        public Builder memoryCache(MemoryCacheAware<String, Bitmap> memoryCache) {
            if (memoryCacheSize != 0) {
                L.w(WARNING_OVERLAP_MEMORY_CACHE);
            }

            this.memoryCache = memoryCache;
            return this;
        }


        public Builder discCacheSize(int maxCacheSize) {
            if (maxCacheSize <= 0)
                throw new IllegalArgumentException("maxCacheSize must be a positive number");

            if (discCache != null || discCacheFileCount > 0) {
                L.w(WARNING_OVERLAP_DISC_CACHE_PARAMS);
            }

            this.discCacheSize = maxCacheSize;
            return this;
        }

        public Builder discCacheFileCount(int maxFileCount) {
            if (maxFileCount <= 0)
                throw new IllegalArgumentException("maxFileCount must be a positive number");

            if (discCache != null || discCacheSize > 0) {
                L.w(WARNING_OVERLAP_DISC_CACHE_PARAMS);
            }

            this.discCacheSize = 0;
            this.discCacheFileCount = maxFileCount;
            return this;
        }

        public Builder discCacheFileNameGenerator(FileNameGenerator fileNameGenerator) {
            if (discCache != null) {
                L.w(WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR);
            }

            this.discCacheFileNameGenerator = fileNameGenerator;
            return this;
        }

        public Builder imageDownloader(ImageDownloader imageDownloader) {
            this.downloader = imageDownloader;
            return this;
        }

        public Builder imageDecoder(ImageDecoder imageDecoder) {
            this.decoder = imageDecoder;
            return this;
        }

        public Builder discCache(DiscCacheAware discCache) {
            if (discCacheSize > 0 || discCacheFileCount > 0) {
                L.w(WARNING_OVERLAP_DISC_CACHE_PARAMS);
            }
            if (discCacheFileNameGenerator != null) {
                L.w(WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR);
            }

            this.discCache = discCache;
            return this;
        }


        public Builder defaultDisplayImageOptions(DisplayImageOptions defaultDisplayImageOptions) {
            this.defaultDisplayImageOptions = defaultDisplayImageOptions;
            return this;
        }

        public Builder enableLogging() {
            this.loggingEnabled = true;
            return this;
        }

        public ImageLoaderConfiguration build() {
            initEmptyFiledsWithDefaultValues();
            return new ImageLoaderConfiguration(this);
        }

        private void initEmptyFiledsWithDefaultValues() {
            if (discCache == null) {
                if (discCacheFileNameGenerator == null) {
                    discCacheFileNameGenerator = DefaultConfigurationFactory.createFileNameGenerator();
                }
                discCache = DefaultConfigurationFactory.createDiscCache(context, discCacheFileNameGenerator, discCacheSize, discCacheFileCount);
            }
            if (memoryCache == null) {
                memoryCache = DefaultConfigurationFactory.createMemoryCache(memoryCacheSize);
            }
            if (denyCacheImageMultipleSizesInMemory) {
//				memoryCache = new FuzzyKeyMemoryCache<String, Bitmap>(memoryCache, MemoryCacheUtil.createFuzzyKeyComparator());
                memoryCache = new LruCacheImpl(4 * 1024 * 1024);
            }
            if (downloader == null) {
                downloader = DefaultConfigurationFactory.createImageDownloader(context);
            }
            if (decoder == null) {
                decoder = DefaultConfigurationFactory.createImageDecoder(loggingEnabled);
            }
            if (defaultDisplayImageOptions == null) {
                defaultDisplayImageOptions = DisplayImageOptions.createSimple();
            }
        }
    }
}
