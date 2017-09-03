package com.sens.baseapplication.ui.widget.listener;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by SensYang on 2017/03/11 13:13
 */
public class DeclaredOnClickListener implements View.OnClickListener {
    private final View mHostView;
    private final String mMethodName;

    private Method mResolvedMethod;
    private Context mResolvedContext;

    public DeclaredOnClickListener(@NonNull View hostView, @NonNull String methodName) {
        mHostView = hostView;
        mMethodName = methodName;
    }

    @Override
    public void onClick(@NonNull View v) {
        if (mResolvedMethod == null) {
            resolveMethod(mHostView.getContext(), mMethodName);

            mResolvedMethod.setAccessible(true);
        }

        try {

            mResolvedMethod.invoke(mResolvedContext, v);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not execute non-public method for android:onClick", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Could not execute method for android:onClick", e);
        }
    }

    @NonNull
    private void resolveMethod(@Nullable Context context, @NonNull String name) {
        if (context != null) {
            try {
                if (!context.isRestricted()) {
                    final Method method = context.getClass().getMethod(mMethodName, View.class);
                    if (method != null) {
                        mResolvedMethod = method;
                        mResolvedContext = context;
                        return;
                    }

                }
            } catch (NoSuchMethodException e) {
                // Failed to find method, keep searching up the hierarchy.
            }

            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                // Can't search up the hierarchy, null out and fail.
                context = null;
            }
        }
    }
}
