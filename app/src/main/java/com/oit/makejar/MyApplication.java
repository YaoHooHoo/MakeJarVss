package com.oit.makejar;

import android.app.Application;

import com.oit.slaudio.AudioManage;

/**
 * Created by Yao on 2018/2/8.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        //报错抓取异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

    }
}
