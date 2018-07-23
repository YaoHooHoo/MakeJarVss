package com.oit.slaudio;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;

import com.oit.utils.AppManager;
import com.oit.utils.ScreenUtils;

public class ScreenShot {

    private static int width;
    private static int height;

    private static Activity lastActivity = null;

    private static int[] bitArray = null;

    public static void takeScreenShot() {
        Activity currentActivity = AppManager.getAppManager().currentActivity();
        if (currentActivity != lastActivity) {
            lastActivity = currentActivity;
        }
        if (null == lastActivity) return;
        lastActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View view = lastActivity.getWindow().getDecorView();
                view.buildDrawingCache();
                shotActivityNoStatusBar(lastActivity);
            }
        });
        AudioManage.screenCapImage(bitArray, width, height);
    }

    public static void initImageData(Context activity) {
        width = ScreenUtils.getScreenWidth(activity);
        height = ScreenUtils.getScreenHeight(activity);
        bitArray = new int[width * height * 2];
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight(); //earlier version
    }

    /**
     * 根据指定的Activity截图（去除状态栏）
     *
     * @param activity 要截图的Activity
     * @return Bitmap
     */
    public static void shotActivityNoStatusBar(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        Bitmap bmp = view.getDrawingCache();
        if (null != bmp && null != bitArray) {
            bmp.getPixels(bitArray, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        }
        // 销毁缓存信息
        view.destroyDrawingCache();
    }

    public static void bitArrayDestroy() {
        bitArray = null;
    }
}
