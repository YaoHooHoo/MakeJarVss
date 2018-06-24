package com.oit.threadutils;

public class ServiceThread extends Thread {

    protected boolean mbRunning;            //运行标志

    public ServiceThread() {
        mbRunning = false;
    }

    @Override
    public void run() {

        boolean bSuccess = false;
        try {
            bSuccess = OnStartThreadEvent();

        } catch (Exception ex) {
            ex.printStackTrace();
            bSuccess = false;
        }
        //运行线程序
        if (bSuccess) {
            while (mbRunning) {
                try {
                    if (ThreadFunction() == false) break;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ServiceThread run exception:" + ex.getMessage());
                }
            }
        }
        try {

            OnStopThreadEvent();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //开始线程
    public void StartThread() {

        mbRunning = true;
        this.start();
    }

    //停止线程
    public void StopThread() {

        mbRunning = false;
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

        return true;
    }
}
