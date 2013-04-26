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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import cn.link.imageloader.assist.ImageScaleType;
import cn.link.imageloader.display.BitmapDisplayer;
import cn.link.imageloader.display.BitmapProgressListener;

public final class DisplayImageOptions {
    private final int mResetImage;
    private final boolean mIfCacheInMemory;
    private final boolean mIfCacheOnDisc;
    private final ImageScaleType mImageScaleType;
    private final Options mDecodingOptions;
    private final Bitmap.CompressFormat imageCompressFormatForDiscCache;
    private final int imageQualityForDiscCache;
    private final BitmapDisplayer mDisplayer;

    private final BitmapProgressListener mProgressListener;
    private final boolean mDispalyIfInMemory;
    private final String mDisplayUrl;

    private DisplayImageOptions(Builder builder) {
        mResetImage = builder.mResetImage;
        mIfCacheInMemory = builder.mIfCacheInMemory;
        mIfCacheOnDisc = builder.mIfCacheOnDisc;
        mImageScaleType = builder.mImageScaleType;
        mDecodingOptions = builder.mDecodingOptions;
        mDisplayer = builder.mDisplay;
        mProgressListener = builder.mProgressListener;
        mDispalyIfInMemory = builder.mDispalyIfInMemory;
        mDisplayUrl = builder.mDisplayUrl;
    }

    public int getResetImage() {
        return mResetImage;
    }

    public boolean ifCacheInMemory() {
        return mIfCacheInMemory;
    }

    public boolean ifCacheOnDisc() {
        return mIfCacheOnDisc;
    }

    public ImageScaleType getImageScaleType() {
        return mImageScaleType;
    }

    public Options getDecodingOptions() {
        return mDecodingOptions;
    }

    public BitmapDisplayer getDisplayer() {
        return mDisplayer;
    }

    public BitmapProgressListener getProgressListener() {
        return mProgressListener;
    }

    public boolean isDispalyIfInMemory() {
        return mDispalyIfInMemory;
    }

    public String getDisplayUrl() {
        return mDisplayUrl;
    }

    public static class Builder {
        private int mResetImage = 0;
        private boolean mIfCacheInMemory = true;
        private boolean mIfCacheOnDisc = true;
        private ImageScaleType mImageScaleType = ImageScaleType.IN_SAMPLE_POWER_OF_2;
        private Options mDecodingOptions = new Options();
        private BitmapDisplayer mDisplay = DefaultConfigurationFactory.createBitmapDisplayer();

        private BitmapProgressListener mProgressListener;
        private boolean mDispalyIfInMemory;
        private String mDisplayUrl;

        public Builder() {
            mDecodingOptions.inPurgeable = true;
            mDecodingOptions.inInputShareable = true;
        }

        public Builder setResetImage(int img) {
            this.mResetImage = img;
            return this;
        }


        public Builder disableCacheInMemory() {
            mIfCacheInMemory = false;
            return this;
        }


        public Builder disableCacheOnDisc() {
            mIfCacheOnDisc = false;
            return this;
        }


        public Builder setImageScaleType(ImageScaleType imageScaleType) {
            this.mImageScaleType = imageScaleType;
            return this;
        }


        public Builder setDecodingOptions(Options decodingOptions) {
            this.mDecodingOptions = decodingOptions;
            return this;
        }


        public Builder setDisplayer(BitmapDisplayer displayer) {
            this.mDisplay = displayer;
            return this;
        }

        public Builder setProgressListener(BitmapProgressListener listener) {
            this.mProgressListener = listener;
            return this;
        }

        public Builder setDisplayIfInMemory(boolean display) {
            this.mDispalyIfInMemory = display;
            return this;
        }

        public Builder setDisplayUrl(String url) {
            this.mDisplayUrl = url;
            return this;
        }


        public Builder cloneFrom(DisplayImageOptions options) {
            mResetImage = options.mResetImage;
            mIfCacheInMemory = options.mIfCacheInMemory;
            mIfCacheOnDisc = options.mIfCacheOnDisc;
            mImageScaleType = options.mImageScaleType;
            mDecodingOptions = options.mDecodingOptions;
            mDisplay = options.mDisplayer;
            mProgressListener = options.mProgressListener;
            mDispalyIfInMemory = options.mDispalyIfInMemory;
            mDisplayUrl = options.mDisplayUrl;
            return this;
        }


        public DisplayImageOptions build() {
            return new DisplayImageOptions(this);
        }
    }


    public static DisplayImageOptions createSimple() {
        return new Builder().build();
    }
}
