package com.oit.slaudio;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.oit.utils.AppManager;
import com.oit.utils.LogToFile;

import java.util.HashMap;
import java.util.Map;

public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {

    public static int activitySize = 1;
    public static boolean isBackFont = true;
    private String TAG = "MyLifecycleHandler";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e("MyLifecycle----", "------1");
        AppManager.getAppManager().addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (AudioManage.isFirstLifeListener)AudioManage.isFirstLifeListener = false;
        Log.e("MyLifecycle----", "------2");
        activitySize ++;
        if(activitySize>=1){
            isBackFont = true;
        } else {
            isBackFont = false;
        }
        Log.e("MyLifecycleHandler-bool", "" + isBackFont);
        LogToFile.e("MyLifecycleHandler-bool-start", "" + isBackFont);
        Log.e("MyLifecycleHandler-star", "" + activitySize);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e("MyLifecycle----", "------3");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e("MyLifecycle----", "------4");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e("MyLifecycle----", "------5");
        activitySize --;
        if(activitySize>=1){
            isBackFont = true;
        } else {
            isBackFont = false;
        }
        Log.e("MyLifecycleHandler-bool", "" + isBackFont);
        LogToFile.e("MyLifecycleHandler-bool-stop", "" + isBackFont);
        Log.e("MyLifecycleHandler-stop", "" + activitySize);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("MyLifecycle----", "------6");
        AppManager.getAppManager().removeActivity(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e("MyLifecycle----", "------7");
    }
}
