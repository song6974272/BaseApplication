package com.sens.baseapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sens.baseapplication.BaseApplication;
import com.sens.baseapplication.R;
import com.sens.baseapplication.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2017/7/3 0003.
 */

public class NetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            ToastUtil.showLongToast(R.string.none_net_availabel);
            for (WeakReference<NetStateCallback> netStateCallback : callbackList) {
                if (netStateCallback != null && netStateCallback.get() != null)
                    netStateCallback.get().netAvailable(false);
            }
        } else if (networkInfo != null && networkInfo.isAvailable()) {
            for (WeakReference<NetStateCallback> netStateCallback : callbackList) {
                if (netStateCallback != null && netStateCallback.get() != null)
                    netStateCallback.get().netAvailable(true);
            }
        }
    }

    private static List<WeakReference<NetStateCallback>> callbackList = new ArrayList<>();

    public static void addCallback(NetStateCallback callback) {
        callbackList.add(new WeakReference<>(callback));
    }

    public static void removeCallback(NetStateCallback callback) {
        for (WeakReference<NetStateCallback> weakReference : callbackList) {
            if (weakReference != null && weakReference.get() != null && weakReference.get().equals(callback)) {
                callbackList.remove(weakReference);
                break;
            }
        }
    }

    public static void clearCallback() {
        callbackList.clear();
    }

    public interface NetStateCallback {
        void netAvailable(boolean isAvailable);
    }
}
