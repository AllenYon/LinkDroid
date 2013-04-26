package cn.link.utils;

import android.os.Environment;

import java.io.File;

public interface Constants {

    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "linkDroid";
    public static final String CACHE_PATH = BASE_PATH + File.separator + ".cache";
    public static final String CAMERA_PATH = BASE_PATH + File.separator + ".camera";

}
