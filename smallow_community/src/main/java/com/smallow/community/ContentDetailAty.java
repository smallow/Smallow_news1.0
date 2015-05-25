package com.smallow.community;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by smallow on 2015/5/25.
 */
public class ContentDetailAty extends Activity {
    private Integer contentId;

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_content_detail);
        /*Intent intent=getIntent();
        if(intent!=null)
            contentId=intent.getIntExtra("contentId",0);*/

        textView= (TextView) findViewById(R.id.ttTest);
        textView.setText("test");
    }
}
