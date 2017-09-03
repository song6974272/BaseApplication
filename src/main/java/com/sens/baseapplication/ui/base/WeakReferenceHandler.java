package com.sens.baseapplication.ui.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class WeakReferenceHandler extends Handler {
    private WeakReference<BaseActivity> weakActivity = null;
    private WeakReference<BaseFragment> weakFragment = null;

    public WeakReferenceHandler(BaseActivity activity) {
        super(Looper.getMainLooper());
        weakActivity = new WeakReference<>(activity);
    }

    public WeakReferenceHandler(BaseFragment fragment) {
        super(Looper.getMainLooper());
        weakFragment = new WeakReference<>(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        if (null != weakActivity && null != weakActivity.get()) {
            if (weakActivity.get().isFinishing()) return;
            weakActivity.get().handlerPacketMsg(msg);
        } else if (null != weakFragment && null != weakFragment.get()) {
            weakFragment.get().handlerPacketMsg(msg);
        }
    }

    public void onDestroy() {
        weakActivity = null;
        weakFragment = null;
    }
}
