package com.huxwd.card.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * 监听网络状态变化
 */
public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (onNetworkChangedListener != null) {
                onNetworkChangedListener.onNetworkChanged();
            }
        }
    }

    public interface OnNetworkChangedListener {
        void onNetworkChanged();
    }

    private OnNetworkChangedListener onNetworkChangedListener;

    public void setOnNetworkChangedListener(OnNetworkChangedListener onNetworkChangedListener) {
        this.onNetworkChangedListener = onNetworkChangedListener;
    }
}