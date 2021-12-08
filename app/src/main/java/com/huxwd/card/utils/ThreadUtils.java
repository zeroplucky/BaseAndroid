package com.huxwd.card.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 创建日期：2021/9/23 17:40
 *
 * @author zhang_
 * @version 1.0
 * 包名： com.huxwd.card.utils
 * 类说明：
 */

public class ThreadUtils {

    public static final String TAG = "ThreadUtils";

    private static Executor sExecutor = Executors.newSingleThreadExecutor();

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        sHandler.post(runnable);
    }

    public static void runOnUiThread(Runnable runnable, long delayMillis) {
        sHandler.postDelayed(runnable, delayMillis);
    }

    public static void runOnBackgroundThread(Runnable runnable) {
        sExecutor.execute(runnable);
    }

}
