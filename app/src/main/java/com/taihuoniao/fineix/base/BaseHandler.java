package com.taihuoniao.fineix.base;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by taihuoniao on 2016/5/9.
 */
public class BaseHandler extends Handler {
    private WeakReference<BaseActivity> activityWeakReference;
    private WeakReference<BaseFragment> fragmentWeakReference;

    public BaseHandler(BaseActivity activity) {
        activityWeakReference = new WeakReference<BaseActivity>(activity);
    }

    public BaseHandler(BaseFragment fragment) {
        fragmentWeakReference = new WeakReference<BaseFragment>(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        if (activityWeakReference == null) {
            if (fragmentWeakReference == null)
                return;
            if (fragmentWeakReference.get() == null)
                return;
            fragmentWeakReference.get().handMessage(msg);
        } else if (fragmentWeakReference == null) {
            if (activityWeakReference.get() == null)
                return;
            activityWeakReference.get().handMessage(msg);
        } else {
            if (activityWeakReference.get() == null) {
                if (fragmentWeakReference.get() == null) {
                    return;
                }
                fragmentWeakReference.get().handMessage(msg);
            } else {
                activityWeakReference.get().handMessage(msg);
                if (fragmentWeakReference.get() == null)
                    return;
                fragmentWeakReference.get().handMessage(msg);
            }

        }

    }

}
