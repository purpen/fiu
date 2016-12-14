package com.taihuoniao.fineix.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.DialogHelp;

/**
 * Created by Stephen on 2016/12/13 15:48
 * Email: 895745843@qq.com
 */

public class BaseDialog extends Dialog{
    protected Context mContext;
    protected LinearLayout mRootLinearLayout;

    public BaseDialog(Context context) {
        this(context, 0);
    }

    public BaseDialog(Context context, int theme) {
        super(context, R.style.AlertDialog);
        this.mContext = context;
//        mRootLinearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_base_layout, null, false);
        initView();
    }

    protected void initView(){
//        this.setContentView(mRootLinearLayout);
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.gravity = Gravity.CENTER;
//        lp.width = (int) (MainApplication.getContext().getScreenWidth() * 0.75); // 设置宽度
//        getWindow().setAttributes(lp);
//        this.setCanceledOnTouchOutside(false);
    }
}
