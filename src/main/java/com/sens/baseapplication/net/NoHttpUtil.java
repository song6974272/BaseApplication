package com.sens.baseapplication.net;

import com.sens.baseapplication.BaseApplication;
import com.sens.baseapplication.R;
import com.sens.baseapplication.utils.ToastUtil;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.tools.NetUtil;

/**
 * Created by SensYang on 2017/03/14 9:13
 */
public class NoHttpUtil {
    protected RequestQueue requestQueue;
    private static NoHttpUtil instance;

    public static NoHttpUtil getInstance() {
        if (instance == null) {
            instance = new NoHttpUtil();
        }
        return instance;
    }

    public <T> void add(Request<T> request) {
        add(-1, request);
    }

    public <T> void add(int what, Request<T> request) {
        add(what, request, null);
    }

    public <T> void add(Request<T> request, OnResponseListener<T> responseListener) {
//        if (responseListener != null && request instanceof BaseBeanRequest) {
//            ((BaseBeanRequest) request).setBeanClass((Class) ((ParameterizedType) responseListener.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
//        }
        Object cancelSign;
        if (BaseApplication.getInstance().getTopActivity() == null)
            cancelSign = "nohttp";
        else
            cancelSign = BaseApplication.getInstance().getTopActivity().hashCode();
        add(cancelSign, request, responseListener);
    }

    public <T> void add(Object cancelSign, Request<T> request, OnResponseListener<T> responseListener) {
        if (NetUtil.isNetworkAvailable()) {
            requestQueue.add(cancelSign, request, responseListener);
        } else ToastUtil.showToast(R.string.none_net_availabel);
    }

    protected NoHttpUtil() {
        initRequestQueue();
    }

    public void cancelBytag(Object o) {
        requestQueue.cancelBySign(o);
    }

    protected void initRequestQueue() {
        requestQueue = new RequestQueue(3);
        requestQueue.start();
    }

    public void restart() {
        requestQueue.start();
    }
}
