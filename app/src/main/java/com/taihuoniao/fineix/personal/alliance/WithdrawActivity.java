package com.taihuoniao.fineix.personal.alliance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Stephen on 2017/1/16 17:07
 * Email: 895745843@qq.com
 */

public class WithdrawActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.editText1)
    EditText editText1;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.textView_link1)
    TextView textViewLink1;
    @Bind(R.id.button_commit)
    Button buttonCommit;

    public WithdrawActivity() {
        super(R.layout.activity_alliance_withdraw);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "提现");
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.textView_link1, R.id.button_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_link1:
                Toast.makeText(this, "全部提现", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_commit:
                Toast.makeText(this, "确认提现", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
