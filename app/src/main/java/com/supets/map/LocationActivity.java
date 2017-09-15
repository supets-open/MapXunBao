package com.supets.map;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/***
 * 单点定位示例，用来展示基本的定位结果，配置在LocationService.java中
 * 默认配置也可以在LocationService中修改
 * 默认配置的内容自于开发者论坛中对开发者长期提出的疑问内容
 *
 * @author baidu
 *
 */
public class LocationActivity extends BaseLocationActivity {
    private TextView LocationResult;
    private Button startLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationResult = findViewById(R.id.textView1);
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        startLocation = findViewById(R.id.addfence);
        startLocation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (startLocation.getText().toString().equals("开始定位")) {
                    locationService.start();// 定位SDK
                    // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
                    startLocation.setText("停止定位");
                } else {
                    locationService.stop();
                    startLocation.setText("开始定位");
                }
            }
        });
    }

    public void logMsg(String str) {
        final String s = str;
        try {
            if (LocationResult != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocationResult.post(new Runnable() {
                            @Override
                            public void run() {
                                LocationResult.setText(s);
                            }
                        });

                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
