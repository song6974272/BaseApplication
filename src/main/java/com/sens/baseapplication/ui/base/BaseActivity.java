package com.sens.baseapplication.ui.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.sens.baseapplication.R;

/**
 * Activity的基类
 */
@SuppressLint("Registered")
public class BaseActivity extends FragmentActivity {
    private WeakReferenceHandler handler;

    private boolean destroyed;

    private int barColor;

    public void setBarColor(int color) {
        barColor = color;
    }

    public boolean isDestroyed() {
        if (destroyed) return destroyed;
        return super.isDestroyed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (barColor > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setStatusBarColor(getResources().getColor(barColor));
            }
        }
    }

    @Override
    protected void onDestroy() {
        destroyed = true;
        super.onDestroy();
    }

    public WeakReferenceHandler getHandler() {
        if (handler == null) handler = new WeakReferenceHandler(this);
        return this.handler;
    }

    protected void handlerPacketMsg(Message msg) {
    }

    protected void showFragment(int contentId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(contentId, fragment).commitAllowingStateLoss();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.open_enter, R.anim.open_exit);
    }

    @Override
    public void finish() {
        destroyed = true;
        super.finish();
        overridePendingTransition(R.anim.close_enter, R.anim.close_exit);
    }
}