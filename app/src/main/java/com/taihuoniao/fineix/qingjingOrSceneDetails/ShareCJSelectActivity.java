package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/25.
 */
public class ShareCJSelectActivity extends BaseActivity {
    @Bind(R.id.activity_share_select_img)
    ImageView imageView;

    public ShareCJSelectActivity() {
        super(R.layout.activity_share_select);
    }

    @Override
    protected void initView() {
        byte[] bytes = getIntent().getByteArrayExtra("bytes");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
    }
}
