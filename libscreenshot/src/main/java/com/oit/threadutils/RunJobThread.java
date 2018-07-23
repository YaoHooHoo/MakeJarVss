package com.oit.threadutils;

import android.util.Log;

import com.oit.slaudio.AudioManage;
import com.oit.utils.LogToFile;

import java.util.HashMap;
import java.util.Map;

import static com.oit.threadutils.OptData.OptDataType.ODT_END;
import static com.oit.threadutils.OptData.OptDataType.ODT_START;


//运行job线程
public class RunJobThread extends com.oit.threadutils.ServiceThread {


    private int mThreadIndex;//线程索引
    private IRunJobData mIRunJobData;//数据接口
    private Map<Integer, Object> mOptDataMap;//操作数据map
    private String TAG = "AudioManage";

    public RunJobThread() {
        mIRunJobData = null;
        mOptDataMap = new HashMap<>();
    }

    //初始化线程类
    public void InitRunJobThread(int nIndex, IRunJobData objRunInterface) {
        mThreadIndex = nIndex;
        mIRunJobData = objRunInterface;
    }

    //线程序开始事件
    protected boolean OnStartThreadEvent() {
        return true;
    }

    //线程停止事件
    protected boolean OnStopThreadEvent() {
        return true;
    }

    //线程执行函数
    protected boolean ThreadFunction() {

        if (mIRunJobData == null) return false;

        Log.e(TAG, "thread start--1-");
        Object objJob = mIRunJobData.GetJobItemData();
        if (objJob == null) {
            Log.e(TAG, "----Bowen.hu,ThreadFunction start get job item failed,job item is null");
            return mbRunning;
        }

        OptData jobData = (OptData) objJob;
        Log.e(TAG, "get data success--2-");
        if (jobData.nDataType == ODT_START) {
            //执行开始辅助函数
            CreateUserOrder(jobData);
        } else if (jobData.nDataType == ODT_END) {
            //执行结束辅助函数
            SubmitOrderList(jobData);
        }
        Log.e(TAG, "thread finish--3-");
        return mbRunning;
    }

    //开始视频辅助
    private boolean CreateUserOrder(final OptData jobData) {
        LogToFile.d(TAG, "audio manage start media function--3--");
        AudioManage.setBusinessType(jobData.nType, jobData.extValue);
        AudioManage.startAvtMedia(640, 480, jobData.screenWidth, jobData.screenHeight, jobData.jobNum, jobData.mobile);
        return true;
    }

    //结束视频辅助
    private boolean SubmitOrderList(final OptData jobData) {
        LogToFile.d(TAG, "audio manage end media function--2--");
        AudioManage.stopAvtMedia();
        return true;
    }
}
