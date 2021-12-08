package com.sunfusheng;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author sunfusheng on 2018/2/17.
 */
public class FirAppInfo {

    public static class AppInfo {
        // 添加的信息
        public int appSize;
        public int appVersionCode;
        public String appVersionName;
        public String apkName;
        public String apkPath;
        public String apkInstallUrl;
        public String updateDesc;
        public boolean forceUpDater;

    }


}
