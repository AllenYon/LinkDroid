package cn.link.imageloader.cache.disc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cn.link.imageloader.DefaultConfigurationFactory;
import cn.link.imageloader.DisplayImageOptions;
import cn.link.imageloader.LKImageView;
import cn.link.imageloader.assist.FileNameGenerator;
import cn.link.imageloader.assist.IoUtils;
import cn.link.imageloader.decode.ImageDecoder;

import java.io.*;

/**
 * Base disc cache. Implements common functionality for disc cache.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see DiscCacheAware
 * @see FileNameGenerator
 * @since 1.0.0
 */
public class BaseDiscCache implements DiscCacheAware {

    private static final String ERROR_ARG_NULL = "\"%s\" argument must be not null";

    protected File mCacheDir;

    private FileNameGenerator mFileNameGenerator;

    public BaseDiscCache(File cacheDir) {
        this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator());
    }

    public BaseDiscCache(File cacheDir, FileNameGenerator fileNameGenerator) {
        if (cacheDir == null) {
            throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
        }
        if (fileNameGenerator == null) {
            throw new IllegalArgumentException("fileNameGenerator" + ERROR_ARG_NULL);
        }

        this.mCacheDir = cacheDir;
        this.mFileNameGenerator = fileNameGenerator;
    }

    @Override
    public Bitmap read(String key, DisplayImageOptions options, ImageDecoder decoder) throws IOException {
        File cacheFile = new File(mCacheDir, mFileNameGenerator.generate(key));
        if (cacheFile.exists()) {
            BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(cacheFile));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IoUtils.copyStream(fileInputStream, output);
            IoUtils.closeSilently(fileInputStream);
            return decoder.decode(output, options);
        }
        return null;
    }


    @Override
    public Bitmap decodeAndWrite(InputStream input, DisplayImageOptions options, ImageDecoder decoder) throws IOException {
        Bitmap bitmap = null;
        String fileName = mFileNameGenerator.generate(options.getDisplayUrl());
        File image = new File(mCacheDir, fileName);
        if (!image.exists()) {
            image.createNewFile();
            BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(image));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf1 = new byte[4098];
            int len1 = 0;
            while ((len1 = input.read(buf1)) != -1) {
                fout.write(buf1, 0, len1);
                output.write(buf1, 0, len1);
            }
            fout.flush();
            fout.close();
            bitmap = decoder.decode(output, options);
        }
        if (input != null) {
            input.close();
        }

        return bitmap;
    }

    @Override
    public void clear() {
        File[] files = mCacheDir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }
}