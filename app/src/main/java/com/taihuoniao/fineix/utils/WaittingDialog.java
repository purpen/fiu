package com.taihuoniao.fineix.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.taihuoniao.fineix.R;


/**
 * Created by taihuoniao on 2016/1/21.
 */
public class WaittingDialog extends Dialog{
    public WaittingDialog(Context context) {
        this(context, R.style.custom_progress_dialog);

    }

    public WaittingDialog(Context context, int theme) {
        super(context, R.style.custom_progress_dialog);
        this.setContentView(R.layout.waitting_dialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
//        this.setCancelable(true);

        this.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!hasFocus) {
            dismiss();
        }
    }



}
