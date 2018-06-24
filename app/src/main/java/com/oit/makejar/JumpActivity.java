package com.oit.makejar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.oit.slaudio.AudioManage;


/**
 * Created by Yao on 2018/1/14.
 */

public class JumpActivity extends Activity {



    private TextView click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);

        //加载so
        AudioManage.loadSo(getApplicationContext());

        click = (TextView) findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JumpActivity.this, MainActivity.class));
            }
        });


        getText((ViewGroup) findViewById(android.R.id.content));
    }

    private void getText(ViewGroup v) {
        int count = v.getChildCount();
        if (count == 0) return;
        for (int i = 0; i < count; i++) {
            final View view = v.getChildAt(i);
            if (view instanceof ViewGroup) {
                getText((ViewGroup) view);
            } else if (view instanceof EditText) {
                ((EditText) view).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!TextUtils.isEmpty(s)) {
                            Log.e("yao--editText输入", s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            } else if (view instanceof TextView) {
                String s = ((TextView) view).getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    Log.e("yao--TextView文本", s);
                }
            }
        }
    }
}
