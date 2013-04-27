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
import cn.link.imageloader.decode.BaseImageDecoder;
import cn.link.imageloader.decode.ImageDecoder;
import cn.link.imageloader.download.ImageDownloader;

public final class ImageLoaderConfiguration {
    final Context context;
    final MemoryCacheAware<String, Bitmap> memoryCache;
    final DiscCacheAware discCache;
    final DiscCacheAware reserveDiscCache;
    final ImageDownloader downloader;
    final ImageDecoder mDecoder;
    final DisplayImageOptions defaultDisplayImageOptions;
    final boolean loggingEnabled;


    private ImageLoaderConfiguration(final Builder builder) {
        context = builder.context;
        discCache = builder.discCache;
        memoryCache = builder.memoryCache;
        defaultDisplayImageOptions = builder.defaultDisplayImageOptions;
        loggingEnabled = builder.loggingEnabled;
        downloader = builder.downloader;
        mDecoder = builder.mDecoder;
        reserveDiscCache = DefaultConfigurationFactory.createReserveDiscCache(context);
    }

    public static ImageLoaderConfiguration createDefault(Context context) {
        return new Builder(context).build();
    }

    public static class Builder {
        private Context context;
        private MemoryCacheAware<String, Bitmap> memoryCache = null;
        private DiscCacheAware discCache = null;
        private FileNameGenerator discCacheFileNameGenerator = null;
        private ImageDownloader downloader = null;
        private DisplayImageOptions defaultDisplayImageOptions = null;

        private boolean loggingEnabled = false;
        public ImageDecoder mDecoder;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Builder discCacheFileNameGenerator(FileNameGenerator fileNameGenerator) {
            this.discCacheFileNameGenerator = fileNameGenerator;
            return this;
        }

        public Builder imageDownloader(ImageDownloader imageDownloader) {
            this.downloader = imageDownloader;
            return this;
        }


        public Builder discCache(DiscCacheAware discCache) {
            this.discCache = discCache;
            return this;
        }

        public Builder decoder(ImageDecoder decoder) {
            this.mDecoder = decoder;
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
                discCache = DefaultConfigurationFactory.createDiscCache(context, discCacheFileNameGenerator);
            }
            if (memoryCache == null) {
                memoryCache = DefaultConfigurationFactory.createMemoryCache(8 * 1024 * 1024);
            }
            if (downloader == null) {
                downloader = DefaultConfigurationFactory.createImageDownloader(context);
            }
            if (defaultDisplayImageOptions == null) {
                defaultDisplayImageOptions = DisplayImageOptions.createSimple();
            }

            if (mDecoder == null) {
                mDecoder = new BaseImageDecoder();
            }
        }
    }
}
