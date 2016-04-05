package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.DataConstants;

/**
 * Created by taihuoniao on 2016/3/22.
 */
public class AddProductActivity extends BaseActivity {
    ImageView img;

    public AddProductActivity() {
        super(R.layout.activity_add_product);
    }

    @Override
    protected void requestNet() {

    }

    @Override
    protected void initList() {

    }

    @Override
    protected void initView() {
//        setContentView(R.layout.activity_add_product);
        img = (ImageView) findViewById(R.id.activity_add_product_img);
    }

    public void click(View view) {
        Intent intent = new Intent();
        //测试使用图片，正常情况返回的是javabean
        //http://frbird.qiniudn.com/product/160126/56a718b93ffca26a098baf2c-5-p500x500.jpg
        intent.putExtra("url", "http://frbird.qiniudn.com/product/160126/56a718b93ffca26a098baf2c-5-p500x500.jpg");
        setResult(DataConstants.RESULTCODE_EDIT_ADDPRODUCT, intent);
        finish();
    }
}
