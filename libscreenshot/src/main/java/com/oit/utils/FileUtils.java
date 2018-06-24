package com.oit.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Yao on 2018/1/15.
 * 文件类
 */

public class FileUtils {

    public static String getSDCardPath() {
        File sdcardDir = null;
        boolean sdcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }

    public static void createFile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            file.mkdirs();
        }
    }
}
