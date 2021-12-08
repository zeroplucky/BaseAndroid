package com.huxwd.card.utils;

import android.app.Activity;
import android.view.Gravity;

import com.hjq.xtoast.XToast;
import com.huxwd.card.R;

/**
 * 创建日期：2021/10/19 16:08
 *
 * @author zhang_
 * @version 1.0
 * 包名： com.huxwd.card.utils
 * 类说明：
 */
public class TopToast {

    public static void showSnack(Activity activity, String content) {
        ThreadUtils.runOnUiThread(() -> {
            new XToast<>(activity)
                    .setDuration(2000)
                    .setGravity(Gravity.TOP)
                    .setContentView(R.layout.toast_custom_view)
                    //.setAnimStyle(android.R.style.Animation_Translucent)
                    .setAnimStyle(R.style.Translucent)
                    .setText(android.R.id.message, content)
                    .show();
        });
        //TTSUtils.getInstance().speak(content);
    }

    public static void showSnack(Activity activity, int resId) {
        ThreadUtils.runOnUiThread(() -> {
            new XToast<>(activity)
                    .setDuration(2000)
                    .setGravity(Gravity.TOP)
                    .setContentView(R.layout.toast_custom_view)
                    //.setAnimStyle(android.R.style.Animation_Translucent)
                    .setAnimStyle(R.style.Translucent)
                    .setText(android.R.id.message, resId)
                    .show();
        });
    }
}
