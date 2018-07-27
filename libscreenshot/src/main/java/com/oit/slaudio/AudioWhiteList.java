package com.oit.slaudio;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.oit.threadutils.OptData;
import com.oit.threadutils.RunThreadManage;
import com.oit.utils.AppManager;
import com.oit.utils.CommonDialog;
import com.oit.utils.CopyFileUtils;
import com.oit.utils.DialogViewHolder;
import com.oit.utils.FileUtils;
import com.oit.utils.IpUtils;
import com.oit.utils.LogToFile;
import com.oit.utils.ScreenUtils;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class AudioManage {

    private static Context mContext;
    private static String TAG = "AudioManage";
    private static volatile AudioManage instance = null;
    private static String savePath;
    private static String lbsSvrStr;
    private static String portNumber = "52238";
    private static OnJniEventCallBack onJniEventCallBack;

    private static MediaProjection mMediaProjection = null;
    private static VirtualDisplay mVirtualDisplay = null;
    private static ImageReader mImageReader = null;
    public static ResultInfo resultInfo = new ResultInfo();
    public static int REQUEST_MEDIA_PROJECTION = 1;

    private static int mScreen_Width;//屏幕宽
    private static int mScreen_Height;//屏幕高
    private static int mScreenDensity;//屏幕分辨率
    private static int[] bitArray;//截取屏幕ARGB数组
    private static int[] bitArraySend;//发送屏幕ARGB数组
    private static long cutNum = 0;//屏幕刷新次数
    public static boolean isShot = false;//是否在录屏中
    private static boolean canScreenShotSure = false;//是否可以录屏
    private static boolean isNotifiDown = true;//通知栏是否下拉 true沒下拉，false下拉了

    private static RunThreadManage runThreadManage;//视频辅助开始结束线程

    /**
     * 构造方法定义为私有的，只能通过getInstance方法获取实例
     */
    private AudioManage() {

    }

    /**
     * 单例双重锁式，同步块内容使多线程同时调用时不会生成多个实例
     *
     * @return
     */
    public static AudioManage getInstance() {
        if (instance == null) {
            synchronized (AudioManage.class) {
                if (instance == null) {
                    instance = new AudioManage();
                }
            }
        }
        return instance;
    }

    /**
     * 设置是否可以log写入文件
     *
     * @param canWrite
     */
    public static void isCanWriteLog(boolean canWrite) {
        LogToFile.isCanWriteLog = canWrite;
    }

    /**
     * 设置模糊区域
     *
     * @param x1 左上角横坐标
     * @param y1 左上角钟作彪
     * @param x2 右下角横坐标
     * @param y2 右下角横坐标
     */
    public static void setVagueRegion(int x1, int y1, int x2, int y2) {
        setMaskArea(x1, y1, x2, y2);
    }

    /**
     * 动态加载so
     */
    public static void loadSo(Context context) {
        File dir = context.getDir("libs", Activity.MODE_PRIVATE);
        Log.e("TAG", dir.getAbsolutePath());
        File distFile = new File(dir.getAbsolutePath() + File.separator + "libOitAVManage.so");

        if (CopyFileUtils.copyFileFromAssets(context, "libOitAVManage.so", distFile.getAbsolutePath())) {
            //使用load方法加载内部储存的SO库
            System.load(distFile.getAbsolutePath());
            Log.e(TAG, "copyFileFromAssets success");
        } else {
            Log.e(TAG, "copyFileFromAssets failed");
        }
    }

    /**
     * 静态加载so
     */
    public static void loadSo() {
        System.loadLibrary("OitAVManage");
    }

    /**
     * 暂停远程协助
     */
    public static void pauseMedia(Activity activity, boolean isPause) {
        if (activity == AppManager.getAppManager().currentActivity()) {
            isNotifiDown = isPause;
        }
    }

    /**
     * 设置一定录屏
     */
    public static void setScreenShotSure(boolean canShot) {
        canScreenShotSure = canShot;
    }

    /**
     * 是否可以录屏
     *
     * @return
     */
    private static boolean canShot() {
        Log.e(TAG, "isShot" + "--" + String.valueOf(isShot) + "--canScreenShotSure--" + String.valueOf
                (canScreenShotSure) + "--isBackFont--" + String.valueOf(MyLifecycleHandler.isBackFont) +
                "--isNotifiDown--" + String.valueOf(isNotifiDown));
        if (isShot) {
            if (canScreenShotSure) {
                if (MyLifecycleHandler.isBackFont) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (MyLifecycleHandler.isBackFont && isNotifiDown) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * 初始化
     *
     * @param application
     * @param lbsSvr      负载均衡服务器IP
     */
    public static void init(Application application, String lbsSvr) {
        LogToFile.init();
        LogToFile.d(TAG, "audio manage init start");
        //监听前后台变化
        application.registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        //初始化OitAVManage
        savePath = FileUtils.getSDCardPath() + "/ScreenShot/Video";
        FileUtils.createFile(savePath);
        //域名解析，判断是否是IP
        if (IpUtils.isIp(lbsSvr)) {
            Log.e(TAG, "audio manage create Avt start");
            //如果是IP格式直接访问
            LogToFile.d(TAG, "audio manage create media start");
            createAvtMedia(lbsSvr, savePath);
        } else {
            //如果是域名，解析成IP再访问
            if (lbsSvr.contains(":")) {
                String[] strs = lbsSvr.split(":");
                lbsSvrStr = strs[0];
                portNumber = strs[1];
            } else {
                lbsSvrStr = lbsSvr;
            }
            new Thread(networkTask).start();
        }
        EventHandler eventHandler = EventHandler.getInstance();
        eventHandler.setOnJniEventListener(new EventHandler.OnJniEventCallBack() {
            @Override
            public void onJniCode(int eventId) {
                if (null != onJniEventCallBack) {
                    onJniEventCallBack.onJniCallBack(eventId);
                }
                Log.e(TAG, "eventId" + eventId);
                if (eventId != 0) {
                    LogToFile.d(TAG, "audio manage eventId" + eventId);
                }
                if (0 == eventId && canShot()) {
                    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        Log.e(TAG, "start take shot < 5.0");
                        ScreenShot.takeScreenShot();
                    } else {
                        Log.e(TAG, "start take shot > 5.0");
                        startCapture();
                    }
                } else if (eventId == JniCode.JNI_SC_2007) {
                    endMedia();
                } else if (JniCode.isError(eventId)) {
                    endMedia();
                }
            }
        });
        Log.e(TAG, "audio manage set handler");
        setEventHandler(eventHandler);

        //注册pageUrl接收的广播
        PageUrlReceiver receiver = new PageUrlReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("oit.PageUrlReceiver");
        LocalBroadcastManager.getInstance(application).registerReceiver(receiver, intentFilter);

        runThreadManage = new RunThreadManage(1);
        runThreadManage.StartManageService();
    }

    /**
     * 解析域名
     */
    private static Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            try {
                Message msg = new Message();
                Bundle data = new Bundle();
                Log.e(TAG, "get internet address port --- 1");
                data.putString("value", InetAddress.getByName(lbsSvrStr).getHostAddress());
                data.putString("portNumber", portNumber);
                data.putString("savePath", savePath);
                msg.setData(data);
                Log.e(TAG, "get internet address port --- 2");
                IpsHandler handler = new IpsHandler();
                handler.sendMessage(msg);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                Log.e(TAG, "解析域名失败");
            }
        }
    };

    /**
     * 域名解析后初始化函数
     */
    private static class IpsHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "get internet address port --- 3");
            Bundle data = msg.getData();
            String val = data.getString("value");
            String portNumber = data.getString("portNumber");
            String savePath = data.getString("savePath");
            LogToFile.d(TAG, "audio manage create media start");
            createAvtMedia(val + ":" + portNumber, savePath);
        }
    }

    /**
     * 5.0以上开始录屏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startMediaProjection(Context context) {
        this.mContext = context;
        Log.e(TAG, "start mediaProjection > 5.0");
        LogToFile.d(TAG, "audio manage start mediaProjection > 5.0");
        //初始化视频录制
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) mContext.getSystemService
                (Context.MEDIA_PROJECTION_SERVICE);
        ((Activity) context).startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_MEDIA_PROJECTION);
    }

    /**
     * 开始录屏
     */
    public void startMedia(Context context, String jobNum, String mobile, int businessType, String extValue) {
        LogToFile.d(TAG, "audio manage start media function--1--");
        if (!isShot) {
            this.mContext = context;
            isShot = true;
            setScreenInfo(context);
            Log.e(TAG, "start media function");
            LogToFile.d(TAG, "audio manage start media function--2--");
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                OptData optData = new OptData();
                optData.jobNum = jobNum;
                optData.mobile = mobile;
                optData.extValue = extValue;
                optData.nType = businessType;
                optData.screenWidth = mScreen_Width;
                optData.screenHeight = mScreen_Height;
                optData.nDataType = OptData.OptDataType.ODT_START;
                runThreadManage.AddJobItemData(optData);
            } else {
                LogToFile.d(TAG, "audio manage start virtual");
                startVirtual();
                OptData optData = new OptData();
                optData.jobNum = jobNum;
                optData.mobile = mobile;
                optData.extValue = extValue;
                optData.nType = businessType;
                optData.screenWidth = mScreen_Width;
                optData.screenHeight = mScreen_Height;
                optData.nDataType = OptData.OptDataType.ODT_START;
                runThreadManage.AddJobItemData(optData);
            }
        }
    }

    /**
     * 设置屏幕信息
     */
    private void setScreenInfo(Context context) {
        ScreenShot.initImageData(context);
        mScreen_Width = ScreenUtils.getScreenWidth(context);
        mScreen_Height = ScreenUtils.getScreenHeight(context);
        mScreenDensity = ScreenUtils.getScreenDensity(context);
        bitArray = new int[mScreen_Width * mScreen_Height];
        bitArraySend = new int[mScreen_Width * mScreen_Height];
    }

    /**
     * 结束录屏
     */
    public static void endMedia() {
        LogToFile.d(TAG, "audio manage end media function--1--");
        if (isShot) {
            isShot = false;
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                OptData optData = new OptData();
                optData.nDataType = OptData.OptDataType.ODT_END;
                runThreadManage.AddJobItemData(optData);
            } else {
                OptData optData = new OptData();
                optData.nDataType = OptData.OptDataType.ODT_END;
                runThreadManage.AddJobItemData(optData);
                stopVirtual();
                tearDownMediaProjection();
            }
        }
    }

    public static void MediaDestory() {
        Log.e(TAG, "media destroy endMedia");
        endMedia();
        Log.e(TAG, "media destroy destroyAvtMedia");
        destroyAvtMedia();
        Log.e(TAG, "media destroy detachEventHandler");
        detachEventHandler();

        ScreenShot.bitArrayDestroy();
        bitArray = null;

        runThreadManage.StopManageService();
    }

    /**
     * 设置jni指令回调
     */
    public void setOnJniEventListener(OnJniEventCallBack onJniEventCallBack) {
        this.onJniEventCallBack = onJniEventCallBack;
    }

    /**
     * jni指令码回调
     */
    public interface OnJniEventCallBack {
        void onJniCallBack(int eventId);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startVirtual() {
        if (mMediaProjection != null) {
            Log.i(TAG, "want to display virtual");
            virtualDisplay();
        } else {
            Log.i(TAG, "start screen capture intent");
            Log.i(TAG, "want to build mediaProjection and display virtual");
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setUpMediaProjection() {
        cutNum = 0;
        Intent mResultData = resultInfo.getIntent();
        int mResultCode = resultInfo.getResult();
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) mContext.getSystemService(Context
                .MEDIA_PROJECTION_SERVICE);
        mMediaProjection = mediaProjectionManager.getMediaProjection(mResultCode, mResultData);
        mImageReader = ImageReader.newInstance(mScreen_Width, mScreen_Height, PixelFormat.RGBA_8888, 1);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Log.e(TAG, "onImageAvailable");
                cutNum++;
                Image image = null;
                try {
                    Log.d(TAG, "get preview image");
                    image = reader.acquireLatestImage();
                    if (cutNum % 15 != 1) {
                        if (null != image) {
                            image.close();
                        }
                        return;
                    }
                    Log.e(TAG, "start capture ----1");
                    if (null == image) {
                        return;
                    }
                    Log.e(TAG, "start capture ----2");
                    int width = image.getWidth();
                    int height = image.getHeight();
                    final Image.Plane[] planes = image.getPlanes();
                    final ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * width;
                    Log.e(TAG, "start capture ----3");
                    Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config
                            .ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);
                    Log.e(TAG, "start capture ----4");
                    Bitmap bitmapCut = Bitmap.createBitmap(bitmap, 0, 0, mScreen_Width, mScreen_Height);
                    if (null != bitArray) {
                        synchronized (bitArray) {
                            if (canShot()) {
                                Log.e(TAG, "get bitmapCut size success");
                                Log.e(TAG, String.valueOf(bitArray.length));
                                bitmapCut.getPixels(bitArray, 0, bitmapCut.getWidth(), 0, 0, bitmapCut.getWidth(),
                                        bitmapCut.getHeight());
                                bitArray.notify();
                            }
                        }
                    }
                    bitmap.recycle();
                    bitmapCut.recycle();
                    Log.e(TAG, "start capture ----5");

                } finally {
                    if (null != image) {
                        image.close();
                    }
                }

            }
        }, null);
        Log.i(TAG, "mMediaProjection defined");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void virtualDisplay() {
        if (null != mMediaProjection && null != mImageReader) {
            if (null != mImageReader.getSurface()) {
                mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                        mScreen_Width, mScreen_Height, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        mImageReader.getSurface(), null, null);
                Log.i(TAG, "virtual displayed");
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void startCapture() {
        synchronized (bitArray) {
            copyIntByte(bitArray, bitArraySend);
            bitArray.notify();
        }
        Log.e(TAG, "copyIntByte success");
        if (!canShot()) {
            return;
        }
        Log.e(TAG, "jni screenCapImage");
        Log.e(TAG, String.valueOf(bitArraySend.length));
        AudioManage.screenCapImage(bitArraySend, mScreen_Width, mScreen_Height);
    }

    /**
     * copy 数组
     *
     * @param src
     * @param dest
     */
    private static void copyIntByte(int[] src, int[] dest) {
        System.arraycopy(src, 0, dest, 0, dest.length);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void tearDownMediaProjection() {
        if (null != mMediaProjection) {
            mMediaProjection.stop();
            mMediaProjection = null;
            Log.i(TAG, "mMediaProjection undefined");
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void stopVirtual() {
        if (null != mVirtualDisplay) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
            Log.i(TAG, "virtual display stopped");
        }
    }

    private CommonDialog stopDialog;
    private int layoutId;
    private OnDialogViewHolder onDialogViewHolder;

    /**
     * 设置停止弹窗
     */
    public void setStopDialog(int layoutId, OnDialogViewHolder onDialogViewHolder) {
        this.layoutId = layoutId;
        this.onDialogViewHolder = onDialogViewHolder;
    }

    public interface OnDialogViewHolder {
        void onConvert(DialogViewHolder holder);
    }

    /**
     * 展示停止弹窗
     */
    public void showStopDialog() {
        if (null != onJniEventCallBack){
            onJniEventCallBack.onJniCallBack(2007);
            LogToFile.e("PageUrlReceiver", "event 2007");
        }
        if (layoutId == 0 || onDialogViewHolder == null) {
            LogToFile.e("PageUrlReceiver", "showAlertDialog");
            showAlertDialog();
        } else {
            LogToFile.e("PageUrlReceiver", "showCommonDialog");
            if (null != stopDialog) {
                stopDialog.showDialog();
            } else {
                stopDialog = new CommonDialog(AppManager.getAppManager().currentActivity(), layoutId) {
                    @Override
                    public void convert(DialogViewHolder holder) {
                        onDialogViewHolder.onConvert(holder);
                    }
                };
                stopDialog.showDialog();
            }
        }
    }

    /**
     * 显示默认弹窗
     */
    public void showAlertDialog() {
        // 创建构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(AppManager.getAppManager().currentActivity());
        // 设置参数
        builder.setMessage("已停止远程协助")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.create().show();
    }

    public static native boolean createAvtMedia(String lbsSvr, String mediaPath);

    //设置业务线类型
    public static native void setBusinessType(int nType, String extValue);

    public static native void startAvtMedia(int codecx, int codecy, int screencx, int screency, String jobNum, String
            mobile);

    public static native void stopAvtMedia();

    public static native void destroyAvtMedia();

    public static native void screenCapImage(int[] imgBuf1, int w1, int h1);

    public static native void screenCapImage1(byte[] imgBuf1, int w1, int h1);

    public static native void addAvtText(String txtContent);

    public static native void detachEventHandler();

    public static native void setEventHandler(EventHandler eventHandler);

    public static native String encryptString(String txtContent);

    //设置模糊区域
    public static native void setMaskArea(int x1, int y1, int x2, int y2);
}
