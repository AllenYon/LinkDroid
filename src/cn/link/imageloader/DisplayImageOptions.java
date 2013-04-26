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
import android.graphics.BitmapFactory.Options;
import cn.link.imageloader.assist.BitmapProcessor;
import cn.link.imageloader.assist.ImageScaleType;
import cn.link.imageloader.display.BitmapDisplayer;
import cn.link.imageloader.display.BitmapProgressListener;
import cn.link.imageloader.display.BitmapProgressListener;

public final class DisplayImageOptions {
    private final int stubImage;
    private final int imageForEmptyUri;
    private final int imageOnFail;
    private final boolean resetViewBeforeLoading;
    private final boolean cacheInMemory;
    private final boolean cacheOnDisc;
    private final ImageScaleType imageScaleType;
    private final Options decodingOptions;
    private final int delayBeforeLoading;
    private final Object extraForDownloader;
    private final BitmapProcessor preProcessor;
    private final BitmapProcessor postProcessor;
    private final BitmapDisplayer displayer;

    private final BitmapProgressListener mProgressListener;
    private final boolean mDispalyIfInMemory;
    private final String mDisplayUrl;

    private DisplayImageOptions(Builder builder) {
        stubImage = builder.stubImage;
        imageForEmptyUri = builder.imageForEmptyUri;
        imageOnFail = builder.imageOnFail;
        resetViewBeforeLoading = builder.resetViewBeforeLoading;
        cacheInMemory = builder.cacheInMemory;
        cacheOnDisc = builder.cacheOnDisc;
        imageScaleType = builder.imageScaleType;
        decodingOptions = builder.decodingOptions;
        delayBeforeLoading = builder.delayBeforeLoading;
        extraForDownloader = builder.extraForDownloader;
        preProcessor = builder.preProcessor;
        postProcessor = builder.postProcessor;
        displayer = builder.displayer;

        mProgressListener = builder.mProgressListener;
        mDispalyIfInMemory = builder.mDispalyIfInMemory;
        mDisplayUrl = builder.mDisplayUrl;

    }

    public boolean shouldShowStubImage() {
        return stubImage != 0;
    }

    public boolean shouldShowImageForEmptyUri() {
        return imageForEmptyUri != 0;
    }

    public boolean shouldShowImageOnFail() {
        return imageOnFail != 0;
    }

    public boolean shouldPreProcess() {
        return preProcessor != null;
    }

    public boolean shouldPostProcess() {
        return postProcessor != null;
    }

    public boolean shouldDelayBeforeLoading() {
        return delayBeforeLoading > 0;
    }

    public int getStubImage() {
        return stubImage;
    }

    public int getImageForEmptyUri() {
        return imageForEmptyUri;
    }

    public int getImageOnFail() {
        return imageOnFail;
    }

    public boolean isResetViewBeforeLoading() {
        return resetViewBeforeLoading;
    }

    public boolean isCacheInMemory() {
        return cacheInMemory;
    }

    public boolean isCacheOnDisc() {
        return cacheOnDisc;
    }

    public ImageScaleType getImageScaleType() {
        return imageScaleType;
    }

    public Options getDecodingOptions() {
        return decodingOptions;
    }

    public int getDelayBeforeLoading() {
        return delayBeforeLoading;
    }

    public Object getExtraForDownloader() {
        return extraForDownloader;
    }

    public BitmapProcessor getPreProcessor() {
        return preProcessor;
    }

    public BitmapProcessor getPostProcessor() {
        return postProcessor;
    }

    public BitmapDisplayer getDisplayer() {
        return displayer;
    }


    /**
     * 如果在内存中，则直接显示
     *
     * @return
     */
    public boolean isDisplayIfInMemory() {
        return mDispalyIfInMemory;
    }

    public String getDisplayUrl() {
        return mDisplayUrl;
    }


    public static class Builder {
        private int stubImage = 0;
        private int imageForEmptyUri = 0;
        private int imageOnFail = 0;
        private boolean resetViewBeforeLoading = false;
        private boolean cacheInMemory = false;
        private boolean cacheOnDisc = false;
        private ImageScaleType imageScaleType = ImageScaleType.IN_SAMPLE_POWER_OF_2;
        private Options decodingOptions = new Options();
        private int delayBeforeLoading = 0;
        private Object extraForDownloader = null;
        private BitmapProcessor preProcessor = null;
        private BitmapProcessor postProcessor = null;
        private BitmapDisplayer displayer = DefaultConfigurationFactory.createBitmapDisplayer();

        private BitmapProgressListener mProgressListener;
        private boolean mDispalyIfInMemory;
        private String mDisplayUrl;

        public Builder() {
            decodingOptions.inPurgeable = true;
            decodingOptions.inInputShareable = true;
        }

        public Builder showStubImage(int stubImageRes) {
            stubImage = stubImageRes;
            return this;
        }


        public Builder showImageForEmptyUri(int imageRes) {
            imageForEmptyUri = imageRes;
            return this;
        }


        public Builder showImageOnFail(int imageRes) {
            imageOnFail = imageRes;
            return this;
        }


        public Builder resetViewBeforeLoading() {
            resetViewBeforeLoading = true;
            return this;
        }


        public Builder cacheInMemory() {
            cacheInMemory = true;
            return this;
        }


        public Builder cacheOnDisc() {
            cacheOnDisc = true;
            return this;
        }


        public Builder imageScaleType(ImageScaleType imageScaleType) {
            this.imageScaleType = imageScaleType;
            return this;
        }


        public Builder bitmapConfig(Bitmap.Config bitmapConfig) {
            decodingOptions.inPreferredConfig = bitmapConfig;
            return this;
        }

        public Builder decodingOptions(Options decodingOptions) {
            this.decodingOptions = decodingOptions;
            return this;
        }

        public Builder delayBeforeLoading(int delayInMillis) {
            this.delayBeforeLoading = delayInMillis;
            return this;
        }

        public Builder extraForDownloader(Object extra) {
            this.extraForDownloader = extra;
            return this;
        }


        public Builder preProcessor(BitmapProcessor preProcessor) {
            this.preProcessor = preProcessor;
            return this;
        }


        public Builder postProcessor(BitmapProcessor postProcessor) {
            this.postProcessor = postProcessor;
            return this;
        }


        public Builder displayer(BitmapDisplayer displayer) {
            this.displayer = displayer;
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
            stubImage = options.stubImage;
            imageForEmptyUri = options.imageForEmptyUri;
            imageOnFail = options.imageOnFail;
            resetViewBeforeLoading = options.resetViewBeforeLoading;
            cacheInMemory = options.cacheInMemory;
            cacheOnDisc = options.cacheOnDisc;
            imageScaleType = options.imageScaleType;
            decodingOptions = options.decodingOptions;
            delayBeforeLoading = options.delayBeforeLoading;
            extraForDownloader = options.extraForDownloader;
            preProcessor = options.preProcessor;
            postProcessor = options.postProcessor;
            displayer = options.displayer;
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
