package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/25.
 */
public class ShareCJSelectActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.activity_share_select_titlelayout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.activity_share_select_img)
    ImageView imageView;

    public ShareCJSelectActivity() {
        super(R.layout.activity_share_select);
    }

    @Override
    protected void initView() {
        titleLayout.setTitleVisible(false);
        titleLayout.setRightTv(R.string.complete,getResources().getColor(R.color.white),this);
        byte[] bytes = getIntent().getByteArrayExtra("bytes");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_continue:
                break;
        }
    }
}
