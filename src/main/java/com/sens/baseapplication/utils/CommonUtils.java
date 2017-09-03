package com.sens.baseapplication.utils;

/**
 * @author xly 判断按钮是否被双击的类
 */
public class CommonUtils {
    private static long lastClick;

    /**
     * 两次点击时间间隔小于指定毫秒
     *
     * @param timeLong 指定毫秒
     */
    public static boolean isFastDoubleClick(long timeLong) {
        long time = System.currentTimeMillis();
        long timeBetween = time - lastClick;
        if (0 < timeBetween && timeBetween < timeLong) {    //两次点击时间小于指定毫秒
            return true;
        }
        lastClick = time;
        return false;
    }

    /**
     * 两次点击时间间隔小于400毫秒
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeBetween = time - lastClick;
        if (0 < timeBetween && timeBetween < 500) {    //两次点击时间小于500毫秒
            return true;
        }
        lastClick = time;
        return false;
    }
}
