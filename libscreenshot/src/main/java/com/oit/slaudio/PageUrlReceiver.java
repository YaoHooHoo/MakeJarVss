package com.oit.slaudio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.oit.utils.LogToFile;

public class PageUrlReceiver extends BroadcastReceiver {

    public PageUrlReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String pageUrl = intent.getStringExtra("pageUrl");
        LogToFile.e("PageUrlReceiver", pageUrl);
        Log.e("PageUrlReceiver", pageUrl);

        boolean isWhite = AudioWhiteList.getInstance().isWhiteUrl(pageUrl);
        LogToFile.e("PageUrlReceiver-boolean", String.valueOf(isWhite));
        if (!isWhite && AudioManage.isShot){
            //停止视频辅助
            AudioManage.getInstance().endMedia();
            AudioManage.getInstance().showStopDialog();
        }
    }
}
