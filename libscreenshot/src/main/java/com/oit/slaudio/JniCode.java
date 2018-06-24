package com.oit.slaudio;

/**
 * Created by Yao on 2018/1/15.
 */

public class JniCode {
    public static final int JNI_ERROR_CODE = 1000;//错误码
    public static final int JNI_ERROR_1001 = 1001;//负载服务器地址无效
    public static final int JNI_ERROR_1002 = 1002;//创建Jni媒体服务器失败
    public static final int JNI_ERROR_1003 = 1003;//没有创建Jin媒体服务实例
    public static final int JNI_ERROR_1004 = 1004;//启动定时器失败
    public static final int JNI_ERROR_1005 = 1005;//启动屏幕通知线程失败
    public static final int JNI_ERROR_1006 = 1006;//启动音频组件失败
    public static final int JNI_ERROR_1007 = 1007;//启动视频编码线程失败
    public static final int JNI_ERROR_1008 = 1008;//连接网络线程失败
    public static final int JNI_ERROR_1009 = 1009;//接通视频失败
    public static final int JNI_ERROR_1010 = 1010;//视频地址无效
    public static final int JNI_ERROR_1011 = 1011;//连接到负载服务器失败
    public static final int JNI_ERROR_1012 = 1012;//连接到视频服务器失败
    public static final int JNI_ERROR_1013 = 1013;//登录到视频服务器失败
    public static final int JNI_ERROR_1014 = 1014;//获取ServiceCode失败
    public static final int JNI_ERROR_1015 = 1015;//客服工号为空
    public static final int JNI_ERROR_1016 = 1016;//手机号为空
    public static final int JNI_ERROR_1017 = 1017;//客服工号太长
    public static final int JNI_ERROR_1018 = 1018;//手机号号太长
    public static final int JNI_ERROR_1019 = 1019;//停止媒体服务器失败
    public static final int JNI_ERROR_1020 = 1020;//启动定时器服务器失败


    public static final int JNI_SC_STATE = 2000;//jni处理状态
    public static final int JNI_SC_2001 = 2001;//开始连接到lbs
    public static final int JNI_SC_2002 = 2002;//开始连接到vss
    public static final int JNI_SC_2003 = 2003;//开始请求VSS IP:PORT
    public static final int JNI_SC_2004 = 2004;//请求VSS IP:PORT成功
    public static final int JNI_SC_2005 = 2005;//开始登录到VSS服务器
    public static final int JNI_SC_2006 = 2006;//登录到VSS服务器成功
    public static final int JNI_SC_2007 = 2007;//客服端停止辅助服务

    public static boolean isError(int eventId) {
        int codeNum = eventId - JNI_ERROR_CODE;
        if (0 <= codeNum && codeNum <= 20) {
            return true;
        } else {
            return false;
        }
    }
}