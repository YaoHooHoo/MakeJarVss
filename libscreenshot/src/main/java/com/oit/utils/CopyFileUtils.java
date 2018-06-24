package com.oit.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyFileUtils {

    private static String TAG = "CopyFileUtils";

    /**
     * 拷贝文件
     *
     * @param context
     * @param fileName
     * @param path
     * @return
     */
    public static boolean copyFileFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            Log.e(TAG, "copyFileFromAssets start");
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "[copyFileFromAssets] IOException " + e.toString());
        }
        return copyIsFinish;
    }
}
