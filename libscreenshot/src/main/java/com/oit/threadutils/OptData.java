package com.oit.threadutils;

//操作数据类
public class OptData {

    public enum OptDataType {
        ODT_START,         //开始视频辅助
        ODT_END            //结束视频辅助
    }

    public OptDataType nDataType;

    //业务线类型
    public int nType;
    //扩展字段
    public String extValue;
    //客户号
    public String jobNum;
    //手机号
    public String mobile;
    //屏幕宽
    public int screenWidth;
    //屏幕高
    public int screenHeight;
}

