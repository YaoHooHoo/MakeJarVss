package com.oit.threadutils;

import java.util.ArrayList;

//运行线程管理类
public class RunThreadManage implements IRunJobData {

    private ArrayList<Object> mJobList;//Job牟象list
    private ArrayList<RunJobThread> mThreadList;//线程对角list
    private int mWaitNum;//等待线程数

    //构造函数
    public RunThreadManage(int nThreadNum) {
        mWaitNum = 0;
        mJobList = new ArrayList<>();
        mThreadList = new ArrayList<>();
        for (int i = 0; i < nThreadNum; i++) {
            RunJobThread jobThread = new RunJobThread();
            mThreadList.add(jobThread);
        }
    }

    //添加Job数据
    public void AddJobItemData(Object objItem) {

        synchronized (mJobList) {
            mJobList.add(objItem);
            mJobList.notifyAll();
        }
    }

    // 获取job数据
    public Object GetJobItemData() {
        Object objItem = null;
        try {
            synchronized (mJobList) {
                while (mJobList.isEmpty()) {
                    mWaitNum++;
                    mJobList.wait();
                    mWaitNum--;
                }
                mJobList.notify();
                if (mJobList.size() == 0) {
                    return objItem;
                }
                objItem = mJobList.get(0);
                mJobList.remove(0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return objItem;
    }

    //获取列表项个数
    public int GetJobListCount() {
        int nCount = 0;
        try {
            synchronized (mJobList) {
                nCount = mJobList.size();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nCount;
    }

    //运行服务
    public boolean StartManageService() {
        int nIndex = 1;
        for (RunJobThread itemThread : mThreadList) {
            itemThread.InitRunJobThread(nIndex++, this);
            itemThread.StartThread();
        }
        return true;
    }

    //停止服务
    public void StopManageService() {
        for (RunJobThread itemThread : mThreadList) {
            itemThread.StopThread();
        }
        synchronized (mJobList) {
            mJobList.clear();
            mJobList.notifyAll();
        }
    }
}
