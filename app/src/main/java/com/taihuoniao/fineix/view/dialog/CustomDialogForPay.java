package com.taihuoniao.fineix.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.taihuoniao.fineix.R;


/**
 * Created by android on 2016/3/13.
 */
public class CustomDialogForPay extends Dialog {
    public CustomDialogForPay(Context context) {
        this(context, R.style.custom_progress_dialog);

    }

    public CustomDialogForPay(Context context, int theme) {
        super(context, R.style.custom_progress_dialog);
        this.setContentView(R.layout.custom_dialog_pay);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
//        this.setCancelable(true);

        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!hasFocus) {
//            dismiss();
        }
    }
}
