package com.darktornado.flashbang;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

        TextView txt = new TextView(this);
        txt.setText("반짝이는 주기 : ");
        txt.setTextSize(18);
        layout.addView(txt);

        String[] menus = {"0.1초", "0.5초", "1초"};
        final int[] delay = {50, 250, 500, 50};
        final RadioGroup radios = new RadioGroup(this);
        for (int n = 0; n < menus.length; n++) {
            RadioButton radio = new RadioButton(this);
            radio.setText(menus[n]);
            radio.setId(n);
            radios.addView(radio);
            if (n == 0) radio.setChecked(true);
        }
        radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                delay[3] = delay[i];
            }
        });
        layout.addView(radios);

        ToggleButton btn = new ToggleButton(this);
        btn.setTextOn("Flash Bang");
        btn.setTextOff("Flash Bang");
        btn.setTransformationMethod(null);
        btn.setChecked(false);
        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggle, boolean onoff) {
                if (onoff) {
                    bang[0] = new Thread(() -> {
                        try {
                            String camera = cm.getCameraIdList()[0];
                            for (; ; ) {
                                cm.setTorchMode(camera, true);
                                Thread.sleep(delay[3]);
                                cm.setTorchMode(camera, false);
                                Thread.sleep(delay[3]);
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

        TextView maker = new TextView(this);
        maker.setText("\n© 2026 Dark Tornado, All rights reserved.\n");
        maker.setTextSize(13);
        maker.setGravity(Gravity.CENTER);
        layout.addView(maker);

        int pad = dip2px(16);
        layout.setPadding(pad, pad, pad, pad);
        ScrollView scroll = new ScrollView(this);
        scroll.addView(layout);
        setContentView(scroll);
    }

    private int dip2px(int dips) {
        return (int) Math.ceil(dips * getResources().getDisplayMetrics().density);
    }

}