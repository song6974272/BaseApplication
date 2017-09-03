package com.sens.baseapplication;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by SensYang on 2017/9/2 0002.
 */

public class BaseApplication extends Application {
    private ArrayList<Activity> activityList = new ArrayList<>();
    private static BaseApplication instance;
    private Resources resources;

    @Override
    public Resources getResources() {
        if (resources == null) resources = super.getResources();
        return resources;
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public void clearActivitys() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }

    /**
     * 关闭栈顶的activity
     *
     * @param closeActivity 被关闭的activity
     */
    public void closeActivity(Class<? extends Activity> closeActivity) {
        Activity activity = null;
        for (int i = activityList.size() - 1; i >= 0; i--) {
            activity = activityList.get(i);
            if (activity.getClass().getName().equalsIgnoreCase(closeActivity.getName())) {
                activity.finish();
                break;
            } else {
                activity = null;
            }
        }
        if (activity != null) activityList.remove(activity);
    }

    /**
     * 关闭该activity之上的所有activity 包含自身一起关闭
     */
    public void closeActivitysTopWithSelf(Collection<Class<? extends Activity>> closeActivity) {
        closeActivitysTopWithSelf((Class<? extends Activity>[]) closeActivity.toArray());
    }

    /**
     * 关闭该activity之上的所有activity 包含自身一起关闭
     */
    @SafeVarargs
    public final void closeActivitysTopWithSelf(Class<? extends Activity>... closeActivity) {
        for (Activity activity : activityList) {
            for (Class clazz : closeActivity) {
                if (activity.getClass() == clazz) {
                    closeActivityTop(true, clazz);
                    return;
                }
            }
        }
    }

    /**
     * 关闭该activity之上的所有activity
     *
     * @param withSelf 是否包含自身一起关闭
     */
    public void closeActivityTop(boolean withSelf, Class<? extends Activity> closeActivity) {
        ArrayList<Activity> closedActivitieList = new ArrayList<>();
        boolean beganClose = false;
        for (Activity activity : activityList) {
            if (!beganClose) {
                if (activity.getClass().getName().equalsIgnoreCase(closeActivity.getName())) {
                    beganClose = true;
                    if (withSelf) {
                        closedActivitieList.add(activity);
                    }
                }
            } else {
                closedActivitieList.add(activity);
            }
        }
        for (int i = closedActivitieList.size() - 1; i >= 0; i--) {
            closedActivitieList.get(i).finish();
        }
        activityList.removeAll(closedActivitieList);
    }

    /**
     * 关闭两个Activity之间的所有Activity
     *
     * @param fromActivity activity堆栈中保留的第一个activity
     * @param toActivity   activity堆栈中保留的第二个activity
     */
    public void closeActivitysBetween(Class<? extends Activity> fromActivity, Class<? extends Activity> toActivity) {
        ArrayList<Activity> finishList = new ArrayList<>();
        String name;
        for (int i = activityList.size() - 1; i >= 0; i--) {
            Activity activity = activityList.get(i);
            name = activity.getClass().getName();
            if (name.equalsIgnoreCase(fromActivity.getName()) || name.equalsIgnoreCase(toActivity.getName())) {
                continue;
            }
            if (name.equalsIgnoreCase(fromActivity.getName()) || name.equalsIgnoreCase(toActivity.getName())) {
                break;
            } else if (name.contains("MainActivity")) {
                break;
            } else {
                activity.finish();
                activity.overridePendingTransition(0, 0);
                finishList.add(activity);
            }
        }
        activityList.removeAll(finishList);
    }

    /**
     * 获取顶部Activity
     */
    public Activity getTopActivity() {
        if (activityList.size() > 0) return activityList.get(activityList.size() - 1);
        else return null;
    }

    /**
     * 在堆栈中获取Activity
     */
    public Activity getActivityFromTask(Class<? extends Activity> activityClass) {
        Activity activity;
        for (int i = activityList.size() - 1; i >= 0; i--) {
            activity = activityList.get(i);
            if (activity.getClass().getName().equalsIgnoreCase(activityClass.getName())) {
                return activity;
            }
        }
        return null;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }


    @Override
    public void onTerminate() {
        clearActivitys();
        super.onTerminate();
        System.exit(0);
    }
}
