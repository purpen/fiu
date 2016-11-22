package com.taihuoniao.fineix.personal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.user.returnGoods.ReturnGoodsFragment;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import butterknife.Bind;

public class SalesReturnActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView custom_head;

    private static final String TITLE = "退货";

    public SalesReturnActivity() {
        super(R.layout.activity_salereturn);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, TITLE);
        WindowUtils.chenjin(this);
    }
}
