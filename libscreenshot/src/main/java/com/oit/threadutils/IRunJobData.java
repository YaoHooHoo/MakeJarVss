package com.oit.threadutils;

//运行job数据接口
public interface IRunJobData {

    //添加Job数据
    void AddJobItemData(Object objItem);

    //获取job数据
    Object GetJobItemData();

    //获取列表项个数
    int GetJobListCount();

}

