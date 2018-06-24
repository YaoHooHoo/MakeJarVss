package com.oit.makejar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Yao on 2018/2/1.
 */

public class JumpActivity2 extends Activity {

    private TextView click;
    private RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);

        click = (TextView) findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JumpActivity2.this, JumpActivity3.class));
            }
        });
        parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);
        parentLayout.setBackgroundColor(Color.parseColor("#ffffff"));
    }
}
