package com.oit.utils;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;


public class AppManagerBack {

    private static Stack<Activity> activityStack;
    private static AppManagerBack instance;

    private AppManagerBack() {
    }


    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public static AppManagerBack getAppManager() {
        if (instance == null) {
            instance = new AppManagerBack();
        }
        return instance;
    }


    public void addActivity(Activity activity) {
        if (activity != null) {
            if (activityStack == null) {
                activityStack = new Stack<Activity>();
            }
            activityStack.add(activity);
        }
    }


    public Activity currentActivity() {
        if (activityStack != null && activityStack.size() != 0) {
            Activity activity = activityStack.lastElement();
            return activity;
        } else {
            return null;
        }
    }


    public void finishActivity() {
        if (activityStack != null) {
            Activity activity = activityStack.lastElement();
            if (activity != null) {
                finishActivity(activity);
            }
        }

    }


    public void finishActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }


    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    public void AppExit(Context context) {
        try {
            finishAllActivity();
            System.exit(0);

        } catch (Exception e) {
            System.exit(0);
        }
    }


    public void Appclear(Activity activity) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activity != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
            }
        }
    }
}

