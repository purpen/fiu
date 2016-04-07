package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

/**
 * Created by taihuoniao on 2016/3/23.
 */
public class SelectStoreActivity extends BaseActivity implements View.OnClickListener {
    private GlobalTitleLayout titleLayout;
    private EditText editText;
    private LinearLayout jingdongLinear, taobaoLinear, tianmaoLinear, yamaxunLinear;

    public static SelectStoreActivity instance;

    public SelectStoreActivity() {
        super(R.layout.activity_select_store);
    }


    @Override
    protected void requestNet() {

    }

    @Override
    protected void initList() {
        titleLayout.setColor(R.color.white);
        titleLayout.setTitle(R.string.add_url);
        titleLayout.setBackImgVisible(false);
        titleLayout.setCancelImgVisible(true);
        jingdongLinear.setOnClickListener(this);
        taobaoLinear.setOnClickListener(this);
        tianmaoLinear.setOnClickListener(this);
        yamaxunLinear.setOnClickListener(this);
    }

    @Override
    protected void initView() {
//        setContentView(R.layout.activity_select_store);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_select_store_title);
        editText = (EditText) findViewById(R.id.activity_select_store_edit);
        jingdongLinear = (LinearLayout) findViewById(R.id.activity_select_store_jingdong);
        taobaoLinear = (LinearLayout) findViewById(R.id.activity_select_store_taobao);
        tianmaoLinear = (LinearLayout) findViewById(R.id.activity_select_store_tianmao);
        yamaxunLinear = (LinearLayout) findViewById(R.id.activity_select_store_yamaxun);
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(SelectStoreActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(SelectStoreActivity.this, SearchURLActivity.class);
        intent.putExtra("search", editText.getText().toString());
        switch (v.getId()) {
            case R.id.activity_select_store_jingdong:
                intent.putExtra("store", DataConstants.JINGDONG);
                break;
            case R.id.activity_select_store_taobao:
                intent.putExtra("store", DataConstants.TAOBAO);
                break;
            case R.id.activity_select_store_tianmao:
                intent.putExtra("store", DataConstants.TIANMAO);
                break;
            case R.id.activity_select_store_yamaxun:
                intent.putExtra("store", DataConstants.YAMAXUN);
                break;
        }
        startActivityForResult(intent, DataConstants.REQUESTCODE_SEARCHSTORE_SEARCHURL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case DataConstants.RESULTCODE_SEARCHSTORE_SEARCHURL:
                    setResult(DataConstants.RESULTCODE_EDIT_SEARCHSTORE, data);
                    finish();
                    break;
            }
        }
    }
}
