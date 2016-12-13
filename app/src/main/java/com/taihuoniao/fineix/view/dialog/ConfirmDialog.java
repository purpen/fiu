package com.taihuoniao.fineix.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.scene.AddProductActivity;

/**
 * Created by Stephen on 2016/12/13 15:48
 * Email: 895745843@qq.com
 */

public class ConfirmDialog extends BaseDialog implements View.OnClickListener {
    private IDialogOnClickListener mIDialogOnClickListener;
    private TextView textViewDialogTitle;
    private TextView textViewDialogContent;
    private Button buttonDialogCancel;
    private Button buttonDialogConfirm;

    private String mTitle;
    private String mContent;
    private String[] mOperationTexts;

    public ConfirmDialog(Context context) {
        this(context, 0);
    }

    public ConfirmDialog(Context context, int theme) {
        super(context, theme);
    }

    public ConfirmDialog(Context context, String title, String content, String[] operationTexts, IDialogOnClickListener dialogOnClickListener) {
        this(context);
        this.mTitle = title;
        this.mContent = content;
        this.mOperationTexts = operationTexts;
        this.mIDialogOnClickListener = dialogOnClickListener;

        setValues();
        setOnListener();
        this.show();
    }

    protected void initView(){
        this.setContentView(R.layout.dialog_confirm_layout);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;

        lp.width = (int) (MainApplication.getContext().getScreenWidth() * 0.87); // 设置宽度
        getWindow().setAttributes(lp);
        this.setCanceledOnTouchOutside(false);

        textViewDialogTitle = (TextView) findViewById(R.id.textView_dialog_title);
        textViewDialogContent = (TextView) findViewById(R.id.textView_dialog_content);
        buttonDialogCancel = (Button) findViewById(R.id.button_dialog_cancel);
        buttonDialogConfirm = (Button) findViewById(R.id.button_dialog_confirm);
    }

    private void setValues() {
        if (mTitle != null) {
            textViewDialogTitle.setText(mTitle);
        }
        if (!TextUtils.isEmpty(mContent)) {
            textViewDialogContent.setText(mContent);
        }
        buttonDialogCancel.setText(mOperationTexts[0]);
        buttonDialogConfirm.setText(mOperationTexts[1]);
    }

    private void setOnListener() {
        buttonDialogCancel.setOnClickListener(this);
        buttonDialogConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_dialog_cancel) {
            this.dismiss();
            if (mIDialogOnClickListener != null) {
                mIDialogOnClickListener.click(v, 0);
            }
        } else if (v.getId() == R.id.button_dialog_confirm){
            this.dismiss();
            if (mIDialogOnClickListener != null) {
                mIDialogOnClickListener.click(v,1);
            }
        }
    }
}
