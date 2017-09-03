package com.sens.baseapplication;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by SensYang on 2017/9/2 0002.
 */

public class ActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    private BaseApplication application;

    public ActivityLifecycleCallback(BaseApplication application) {
        this.application = application;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        application.addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        application.removeActivity(activity);
    }
}
