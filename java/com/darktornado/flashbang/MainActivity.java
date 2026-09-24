package com.darktornado.flashbang;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final CameraManager cm = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        final Thread[] bang = new Thread[1];

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);

        ToggleButton btn = new ToggleButton(this);
        btn.setTextOn("Flash Bang");
        btn.setTextOff("Flash Bang");
        btn.setTransformationMethod(null);
        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggle, boolean onoff) {
                if (onoff) {
                    bang[0] = new Thread(() -> {
                        try {
                            String camera = cm.getCameraIdList()[0];
                            for (; ; ) {
                                cm.setTorchMode(camera, true);
                                Thread.sleep(50);
                                cm.setTorchMode(camera, false);
                                Thread.sleep(50);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    bang[0].start();
                } else {
                    try {
                        bang[0].interrupt();
                        cm.setTorchMode(cm.getCameraIdList()[0], false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        layout.addView(btn);

        TextView txt = new TextView(this);
        txt.setText("\n© 2026 Dark Tornado, All rights reserved.\n");
        txt.setTextSize(13);
        txt.setGravity(Gravity.CENTER);
        layout.addView(txt);

        int pad = dip2px(16);
        layout.setPadding(pad, pad, pad, pad);
        ScrollView scroll = new ScrollView(this);
        layout.addView(scroll);
        setContentView(scroll);
    }

    private int dip2px(int dips) {
        return (int) Math.ceil(dips * getResources().getDisplayMetrics().density);
    }

}