package com.cyanflxy.peekerdemo;

import android.app.Activity;
import android.os.Bundle;

import com.cyanflxy.filepeeker.FilePeeker;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FilePeeker.listFile(this);
    }

}
