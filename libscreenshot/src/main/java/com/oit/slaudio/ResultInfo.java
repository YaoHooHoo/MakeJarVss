package com.oit.slaudio;

import android.content.Intent;
import android.media.projection.MediaProjectionManager;

/**
 * Created by Yao on 2018/1/24.
 */

public class ResultInfo {
    public int result;
    private Intent intent;
    private MediaProjectionManager mMediaProjectionManager;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public MediaProjectionManager getMediaProjectionManager() {
        return mMediaProjectionManager;
    }

    public void setMediaProjectionManager(MediaProjectionManager mMediaProjectionManager) {
        this.mMediaProjectionManager = mMediaProjectionManager;
    }

}
