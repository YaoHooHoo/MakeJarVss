package com.oit.makejar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

/**
 * Created by Yao on 2018/2/1.
 */

public class JumpActivity3 extends Activity {

    private RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);


        parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);
        parentLayout.setBackgroundColor(Color.parseColor("#000000"));
    }
}
