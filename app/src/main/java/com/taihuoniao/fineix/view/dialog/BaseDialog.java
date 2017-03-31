package com.taihuoniao.fineix.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.LinearLayout;

import com.taihuoniao.fineix.R;

/**
 * Created by Stephen on 2016/12/13 15:48
 * Email: 895745843@qq.com
 */

public class BaseDialog extends Dialog{
    protected Context mContext;

    public BaseDialog(Context context) {
        this(context, 0);
    }

    public BaseDialog(Context context, int theme) {
        super(context, R.style.AlertDialog);
        this.mContext = context;
        initView();
    }

    protected void initView(){

    }
}
