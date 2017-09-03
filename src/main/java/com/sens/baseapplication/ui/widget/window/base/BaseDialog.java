package com.sens.baseapplication.ui.widget.window.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.sens.baseapplication.R;


/**
 * Created by SensYang on 2016/7/13 0013.
 */
public class BaseDialog extends Dialog {
    private Object tag;

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Object getTag() {
        return tag;
    }

    public BaseDialog(Context context) {
        this(context, R.style.dialog);
    }

    public BaseDialog(Context context, int style) {
        super(context, style);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
    }

    public void showDelayed(long time) {
        super.show();
        getWindow().getDecorView().postDelayed(dismissRunnable, time);
    }

    @Override
    public void dismiss() {
        getWindow().getDecorView().removeCallbacks(dismissRunnable);
//        try {
//            Field field = Dialog.class.getDeclaredField("mDecor");
//            if (field != null) {
//                field.setAccessible(true);
//                View mDecor = (View) field.get(this);
//                if (mDecor != null) {
//                    mDecor.setVisibility(View.GONE);
//                    return;
//                }
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };
}
