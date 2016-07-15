package com.taihuoniao.fineix.scene;

import android.view.View;
import android.widget.EditText;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.FlowLayout;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/7/15.
 */
public class AddAndEditLabelActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.titlelayout)
    GlobalTitleLayout titlelayout;
    @Bind(R.id.flowlayout)
    FlowLayout flowlayout;
    @Bind(R.id.edit_text)
    EditText editText;

    public AddAndEditLabelActivity() {
        super(R.layout.activity_add_edit_label);
    }

    @Override
    protected void initView() {
        titlelayout.setBackgroundResource(R.color.white);
        titlelayout.setBackImgVisible(false);
        titlelayout.setTitle(R.string.add_label, getResources().getColor(R.color.black333333));
        titlelayout.setRightTv(R.string.confirm, getResources().getColor(R.color.black333333), this);
    }

    @Override
    protected void initList() {
        super.initList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_continue:
                break;
        }
    }
}
