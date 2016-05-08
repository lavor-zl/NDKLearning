package com.lavor.ndklearning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private android.widget.TextView text;
    static {
        System.loadLibrary("lavor");
       }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.text = (TextView) findViewById(R.id.text);
        text.setText(getString());
    }
    public native String getString();

}
