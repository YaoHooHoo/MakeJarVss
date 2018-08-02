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

    private Map<String, Object> mFinalCount = new HashMap<>();
    public static boolean isBackFont = true;
    private String TAG = "MyLifecycleHandler";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e("MyLifecycle----", "------1");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (AudioManage.isFirstLifeListener)AudioManage.isFirstLifeListener = false;
        AppManager.getAppManager().addActivity(activity);
        Log.e("MyLifecycle----", "------2");
        mFinalCount.put(activity.getLocalClassName(), TAG);
        if (mFinalCount.size() > 0) {
            isBackFont = true;
        }
        Log.e("MyLifecycleHandler-bool", "" + isBackFont);
        LogToFile.e("MyLifecycleHandler-bool", "" + isBackFont);
        Log.e("MyLifecycleHandler-star", "" + mFinalCount.size());
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
        AppManager.getAppManager().removeActivity(activity);
        Log.e("MyLifecycle----", "------5");
        mFinalCount.remove(activity.getLocalClassName());
        if (mFinalCount.size() == 0) {
            isBackFont = false;
        }
        Log.e("MyLifecycleHandler-bool", "" + isBackFont);
        LogToFile.e("MyLifecycleHandler-bool", "" + isBackFont);
        Log.e("MyLifecycleHandler-stop", "" + mFinalCount.size());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("MyLifecycle----", "------6");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e("MyLifecycle----", "------7");
    }
}
