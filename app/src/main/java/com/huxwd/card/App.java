package com.huxwd.card;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huxwd.card.data.Responze;
import com.huxwd.card.reciever.NetworkReceiver;
import com.huxwd.card.utils.ActivityLifecycleCallbacksImpl;
import com.huxwd.card.utils.MMKVUtil;
import com.huxwd.card.utils.VibrateUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.litepal.LitePal;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 创建日期：2021/9/23 17:03
 *
 * @author zhang_
 * @version 1.0
 * 包名： com.huxwd.card
 * 类说明：
 * <p>
 */
public class App extends Application {

    public static String BASEURL = null;
    public static App instance;
    private NetworkReceiver networkReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        BASEURL = getResources().getString(R.string.baseUrl);
        instance = this;
        init();
        addNetworkListener();
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    private void init() {
        MMKVUtil.init(instance);
        OkGo.getInstance().init(instance).setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        LitePal.initialize(instance);
        VibrateUtil.init(instance);
        CrashReport.initCrashReport(getApplicationContext(), "d353ed5cae", true);
        UMConfigure.preInit(getApplicationContext(), "6191c0f6e0f9bb492b5aa1c6", "um");
        UMConfigure.init(getApplicationContext(), "6191c0f6e0f9bb492b5aa1c6", "um", UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    /*
     * 监听网络变化
     * */
    private void addNetworkListener() {
        if (networkReceiver == null) {
            networkReceiver = new NetworkReceiver();
            networkReceiver.setOnNetworkChangedListener(() -> {
            });
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            registerReceiver(networkReceiver, intentFilter);
        }
    }

    //声明一个监听Activity们生命周期的接口
    private ActivityLifecycleCallbacksImpl activityLifecycleCallbacks = new ActivityLifecycleCallbacksImpl() {

        int count = 0;

        @Override
        public void onActivityStarted(Activity activity) {
            if (count == 0) {
                //Toast.makeText(activity, "App切到前台", Toast.LENGTH_SHORT).show();
            }
            count++;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            count--;
            if (count == 0) {
                Toast toast = Toast.makeText(activity, null, Toast.LENGTH_SHORT);
                toast.setText("App切到后台");
                toast.show();
            }
        }
    };


}
