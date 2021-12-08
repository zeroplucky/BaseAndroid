package com.sunfusheng;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

/**
 * @author sunfusheng on 2018/2/17.
 */
public class FirUpdater {
    private static Context context;
    private FirAppInfo.AppInfo appInfo;

    private FirDownloader firDownloader;
    private FirNotification firNotification;

    private UpdateDialog dialog;

    private static FirUpdater instance;


    public static synchronized FirUpdater getInstance(Context context) {
        if (instance == null) {
            instance = new FirUpdater(context);
        } else {
            setContext(context);
        }
        return instance;
    }

    public static void setContext(Context context) {
        FirUpdater.context = context;
    }

    private FirUpdater(Context context) {
        this.context = context;
    }

    private void initFirDialog() {
        dialog = new UpdateDialog(context, appInfo);
        dialog.setOnClickDownloadDialogListener(new UpdateDialog.OnClickDownloadDialogListener() {
            @Override
            public void onClickDownload(DialogInterface dialog) {
                downloadApk();
            }

            @Override
            public void onClickBackgroundDownload(DialogInterface dialog) {
                firNotification = new FirNotification().createBuilder(context, true);
                firNotification.setContentTitle(appInfo.apkName);
            }

            @Override
            public void onClickCancelDownload(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public boolean isGoOn() {
        if (firDownloader != null && firDownloader.isGoOn()) {
            Toast.makeText(context, "正在下载【" + appInfo.apkName + "】，请稍后", Toast.LENGTH_LONG).show();
            return true;
        } else return false;
    }

    private void downloadApk() {
        if (firDownloader != null && firDownloader.isGoOn()) {
            Toast.makeText(context, "正在下载【" + appInfo.apkName + "】，请稍后", Toast.LENGTH_LONG).show();
            return;
        }
        dialog.downloading(100, 0);
        firDownloader = new FirDownloader(context.getApplicationContext(), appInfo);
        firDownloader.setOnDownLoadListener(new FirDownloader.OnDownLoadListener() {
            @Override
            public void onProgress(int progress) {
                dialog.downloading(100, progress);
                if (firNotification != null) {
                    firNotification.setContentText("下载更新中..." + progress + "%");
                    firNotification.notifyNotification(progress);
                }
            }

            @Override
            public void onSuccess() {
                dialog.dismiss();
                if (firNotification != null) {
                    firNotification.cancel();
                }
                firNotification = new FirNotification().createBuilder(context, false);
                firNotification.setContentIntent(FirUpdaterUtils.getInstallApkIntent(context, appInfo.apkPath));
                firNotification.setContentTitle(appInfo.apkName);
                firNotification.setContentText("下载完成，点击安装");
                firNotification.notifyNotification();
                FirUpdaterUtils.installApk(context, appInfo.apkPath);
            }

            @Override
            public void onError() {
                dialog.dismiss();
                if (firNotification != null) {
                    firNotification.cancel();
                }
            }
        });
        firDownloader.downloadApk();
    }


    /*
     *  1
     * */
    public void checkVersion(String url, boolean forceUpDater, String appName, String appVersionName, String updateDesc) {
        if (firDownloader != null && firDownloader.isGoOn()) {
            Toast.makeText(context, "正在下载【" + appInfo.apkName + "】，请稍后", Toast.LENGTH_LONG).show();
            return;
        }
        if (appInfo == null) appInfo = new FirAppInfo.AppInfo();
        appInfo.forceUpDater = forceUpDater;
        appInfo.appVersionName = appVersionName;
        appInfo.updateDesc = updateDesc;
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context, "请设置 url", Toast.LENGTH_LONG).show();
            return;
        }
        appInfo.apkInstallUrl = url; // 下载
        //
        FirPermissionHelper.getInstant().requestPermission(context, new FirPermissionHelper.OnPermissionCallback() {
            @Override
            public void onGranted() {
                appInfo.apkName = appName + "-" + appVersionName + ".apk";
                if (TextUtils.isEmpty(appInfo.apkPath)) {
                    appInfo.apkPath = Environment.getExternalStorageDirectory() + File.separator + appInfo.apkName;
                }
                FirUpdaterUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        initFirDialog();
                    }
                });
            }

            @Override
            public void onDenied() {
                Toast.makeText(context, "申请权限未通过", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
