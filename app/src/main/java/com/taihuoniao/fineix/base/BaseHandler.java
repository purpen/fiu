package com.taihuoniao.fineix.base;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;

/**
 * Created by taihuoniao on 2016/3/14.
 * 专门用来处理网络请求失败情况的handler
 */
public class BaseHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case DataConstants.NET_FAIL:
                Toast.makeText(MainApplication.getContext(), R.string.host_failure, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
