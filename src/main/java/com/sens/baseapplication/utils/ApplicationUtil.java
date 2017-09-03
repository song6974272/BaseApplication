package com.sens.baseapplication.utils;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.sens.baseapplication.BaseApplication;

import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by SensYang on 2016/8/17 0017.
 */
public class ApplicationUtil {
    /**
     * 获取软件版本号
     */
    public static int getApplicationVersion() {
        try {
            return BaseApplication.getInstance().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取软件版本号
     */
    public static String getApplicationVersionName() {
        try {
            return BaseApplication.getInstance().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取软件版本号
     */
    public static int getAndroidVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取应用包名
     */
    public static String getPackageName() {
        return BaseApplication.getInstance().getPackageName();
    }

    /**
     * 获取应用名
     */
    public static String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = BaseApplication.getInstance().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    public static String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) BaseApplication.getInstance().getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    public static String getUUID() {
        return android.os.Build.FINGERPRINT;
    }
}
