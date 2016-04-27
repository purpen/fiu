package com.taihuoniao.fineix.user;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.utils.QrCodeUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;

/**
 * @author lilin
 * created at 2016/4/26 14:09
 */
public class MyBarCodeActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;

    @Bind(R.id.iv_bar_code)
    ImageView iv_bar_code;
    public MyBarCodeActivity(){
        super(R.layout.activity_bar_code);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"二维码");
        try {
            Bitmap bitmap = QrCodeUtils.Create2DCode(String.format(
                    "http://www.taihuoniao.com/u/%s", LoginInfo.getUserId()));
            iv_bar_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
