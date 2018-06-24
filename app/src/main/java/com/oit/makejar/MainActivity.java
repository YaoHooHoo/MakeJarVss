package com.oit.makejar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.oit.slaudio.AudioManage;
import com.oit.slaudio.JniCode;

public class MainActivity extends Activity implements View.OnClickListener {

    private AudioManage audioManage;

    private Button end;
    private Button start;
    private Button jump;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        end = (Button) findViewById(R.id.end);
        start = (Button) findViewById(R.id.start);
        jump = (Button) findViewById(R.id.jump);

        end.setOnClickListener(this);
        start.setOnClickListener(this);
        jump.setOnClickListener(this);

        setAudioManage();

        setWebView();
    }

    private void setAudioManage() {
//        audioManage = new AudioManage(getApplication(), MainActivity.this, "222.186.58.233:40000");
//        audioManage = new AudioManage(getApplication(), MainActivity.this, "192.168.9.228:52238");
        audioManage = new AudioManage(getApplication(), MainActivity.this, "103.28.215.253:52238");
        audioManage.setOnJniEventListener(new AudioManage.OnJniEventCallBack() {

            @Override
            public void onJniCallBack(final int eventId) {
                Log.e("OpenSLES", eventId + "");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        jump.setText(eventId+"");
                    }
                });
                if (eventId == JniCode.JNI_SC_2007) {
                    audioManage.endMedia();
                }
            }
        });
        String encryptTxt = AudioManage.encryptString("1234567890");


        Log.e("encryptString", encryptTxt);
    }

    private void setWebView() {
        webView = (WebView) findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        webView.loadUrl("http://www.sina.com.cn/");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioManage.MediaDestory();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AudioManage.REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            } else if (data != null && resultCode != 0) {
                AudioManage.resultInfo.setResult(resultCode);
                AudioManage.resultInfo.setIntent(data);
//                String jobNum = AudioManage.encryptString("MPMIB002");
                String jobNum = AudioManage.encryptString("SZ_00001");
                String mobile = AudioManage.encryptString("13631582495");
//                audioManage.startMedia(jobNum, mobile, 10, "扩展字段");
                audioManage.startMedia("HlBoDqgAcv%2Bb2ITpo5Rjig%3D%3D", "13631582495", 10, "扩展字段");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                audioManage.registerBackFont(getApplication());
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                    String jobNum = AudioManage.encryptString("MPMIB002");
                    String jobNum = AudioManage.encryptString("SZ_00001");
                    String mobile = AudioManage.encryptString("13631582495");
//                audioManage.startMedia("MPMIB002", "13631582495",10, "扩展字段");
                    audioManage.startMedia("HlBoDqgAcv%2Bb2ITpo5Rjig%3D%3D", "13631582495", 10, "扩展字段");
//                    audioManage.startMedia(jobNum, mobile, 10, "扩展字段");
                } else {
                    audioManage.startMediaProjection();
                }
                break;
            case R.id.end:
                audioManage.endMedia();
                break;
            case R.id.jump:
                startActivity(new Intent(this, JumpActivity.class));
                break;
            default:
                break;
        }
    }
}
