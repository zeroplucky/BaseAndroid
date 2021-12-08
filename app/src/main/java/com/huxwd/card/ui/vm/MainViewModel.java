package com.huxwd.card.ui.vm;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.huxwd.card.App;
import com.huxwd.card.R;
import com.huxwd.card.data.ClassroomList;
import com.huxwd.card.db.RoomListDb;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.sunfusheng.FirUpdater;
import com.sunfusheng.FirUpdaterUtils;

import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;

public

/**
 * 创建日期：2021/9/23 16:59
 *
 * @author zhang_
 * @version 1.0
 * 包名： com.huxwd.card.ui.viewmodel
 * 类说明：
 */
class MainViewModel extends ViewModel {

    private Gson gson = new Gson();

    public MutableLiveData roomListInfo = new MutableLiveData<List<RoomListDb>>();

    public void checkNewVersion(Context mContext) {
        try {
            String url = App.BASEURL + "getAppVersion";
            RequestBody body = RequestBody.create(HttpParams.MEDIA_TYPE_JSON, "");
            OkGo.<String>post(url).upRequestBody(body).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    String rl = response.body();
                    try {
                        JSONObject object = new JSONObject(rl);
                        String success = object.getString("success");
                        String msg = object.getString("msg");
                        if ("1".equals(success)) {
                            int appVersion = Integer.parseInt(object.getString("appVersion"));
                            String url = object.getString("url");
                            String appForceUpdate = object.getString("appForceUpdate"); // 是否强制APP用户 更新APK 1、是 0、否
                            int versionCode = FirUpdaterUtils.getVersionCode(mContext);
                            if (appVersion > versionCode) {
                                String apkUrl = url;
                                boolean forceup = true;
                                if ("0".equals(appForceUpdate)) forceup = false;
                                String updateDesc = msg;
                                String appName = mContext.getResources().getString(R.string.app_name);
                                FirUpdater firUpdater = FirUpdater.getInstance(mContext);
                                firUpdater.checkVersion(apkUrl, forceup, appName, "", updateDesc);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    /*
     *获取全部上课教室
     * */
    public void getClassroomList() {
        try {
            String url = App.BASEURL + "getClassroomList";
            JSONObject json = new JSONObject();
            json.put("schoolZone", "");
            RequestBody body = RequestBody.create(HttpParams.MEDIA_TYPE_JSON, json.toString());
            OkGo.<String>post(url).upRequestBody(body).execute(new StringCallback() {
                @Override
                public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                    String rl = response.body();
                    ClassroomList classroomList = gson.fromJson(rl, ClassroomList.class);
                    List<RoomListDb> roomList = classroomList.getRoomList();
                    if (roomList != null) {
                        RoomListDb.saveALL(roomList);
                        roomListInfo.setValue(roomList);
                    } else roomListInfo.setValue(null);
                }

                @Override
                public void onCacheSuccess(Response<String> response) {
                    super.onCacheSuccess(response);
                    onSuccess(response);
                }
            });
        } catch (Exception e) {
            roomListInfo.setValue(null);
        }
    }
}
