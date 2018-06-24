package com.oit.slaudio;


import android.os.Handler;
import android.util.Log;

public class EventHandler {

    private static String TAG = "EventHandle";

    private static EventHandler mInstance;

    private Handler mEventHandler;

    private OnJniEventCallBack callBack;

    EventHandler() {
        mEventHandler = null;
    }

    public static EventHandler getInstance() {
        if (mInstance == null) {
            mInstance = new EventHandler();
        }
        return mInstance;
    }

    public void setHandler(Handler handler) {

        mEventHandler = handler;
    }

    public void removeHandler() {
        if (null != mEventHandler) {
            mEventHandler.removeCallbacksAndMessages(null);
        }
        mEventHandler = null;
    }

    /**
     * 截屏指令
     *
     * @param eventId
     */
    public void onJniNotifyEvent(int eventId) {
        Log.e(TAG, eventId + "");
        if (null != callBack) {
            callBack.onJniCode(eventId);
        }
    }

    /**
     * 设置jni指令回调
     */
    public void setOnJniEventListener(OnJniEventCallBack onJniEventCallBack) {
        this.callBack = onJniEventCallBack;
    }

    public interface OnJniEventCallBack {
        void onJniCode(int eventId);
    }
}
