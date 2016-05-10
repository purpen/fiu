package com.taihuoniao.fineix.product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;

/**
 * Created by taihuoniao on 2016/3/3.
 */
public class TransferTimeActivity extends Activity implements View.OnClickListener {
    //上个界面传递过来的数据
    private String transfer_time;//送货时间类型 a任意时间 b周一周五 c周六周日
    //界面下的控件
    private MyGlobalTitleLayout titleLayout;
    private LinearLayout anyLinear;
    private LinearLayout workLinear;
    private LinearLayout restLinear;
    private ImageView anyImg;
    private ImageView workImg;
    private ImageView restImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transefer_time);
        initView();
        setData();
        selectImg();
    }

    private void setData() {
        transfer_time = getIntent().getStringExtra("transfer_time");
        titleLayout.setTitle("送货时间");
        titleLayout.setRightShopCartButton(false);
        titleLayout.setRightSearchButton(false);
        anyLinear.setOnClickListener(this);
        workLinear.setOnClickListener(this);
        restLinear.setOnClickListener(this);
    }

    private void initView() {
//        StatusBarChange.initWindow(TransferTimeActivity.this);
        titleLayout = (MyGlobalTitleLayout) findViewById(R.id.activity_transfer_time_title);
        anyLinear = (LinearLayout) findViewById(R.id.activity_transfer_time_anylinear);
        workLinear = (LinearLayout) findViewById(R.id.activity_transfer_time_worklinear);
        restLinear = (LinearLayout) findViewById(R.id.activity_transfer_time_restlinear);
        anyImg = (ImageView) findViewById(R.id.activity_transfer_time_anyimg);
        workImg = (ImageView) findViewById(R.id.activity_transfer_time_workimg);
        restImg = (ImageView) findViewById(R.id.activity_transfer_time_restimg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_transfer_time_anylinear:
                transfer_time = "a";
                break;
            case R.id.activity_transfer_time_worklinear:
                transfer_time = "b";
                break;
            case R.id.activity_transfer_time_restlinear:
                transfer_time = "c";
                break;
        }
        selectImg();
        Intent intent = new Intent();
        intent.putExtra("transfer_time", transfer_time);
        setResult(DataConstants.RESULTCODE_TRANSFER_TIME, intent);
        finish();

    }

    private void selectImg() {
        anyImg.setImageResource(R.mipmap.check);
        workImg.setImageResource(R.mipmap.check);
        restImg.setImageResource(R.mipmap.check);
        switch (transfer_time) {
            case "a":
                anyImg.setImageResource(R.mipmap.check_red);
                break;
            case "b":
                workImg.setImageResource(R.mipmap.check_red);
                break;
            case "c":
                restImg.setImageResource(R.mipmap.check_red);
                break;
        }
    }
}
