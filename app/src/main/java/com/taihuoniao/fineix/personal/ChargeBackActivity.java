package com.taihuoniao.fineix.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChargeBackActivity extends BaseActivity {

    private CustomHeadView customHead;
    private EditText editText1;
    private LinearLayout linearLayoutChargebackCause;
    private TextView editText2;
    private EditText editText3;
    private Button buttonCommit;

    private static final String TITLE = "退款";

    public ChargeBackActivity() {
        super(R.layout.activity_chargeback);
    }

    @Override
    protected void initView() {
        customHead = (CustomHeadView) findViewById(R.id.custom_head);
        editText1 = (EditText) findViewById(R.id.editText1);
        linearLayoutChargebackCause = (LinearLayout) findViewById(R.id.linearLayout_chargeback_cause);
        editText2 = (TextView) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        buttonCommit = (Button) findViewById(R.id.button_commit);
        customHead.setHeadCenterTxtShow(true, TITLE);
        WindowUtils.chenjin(this);
    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }

    @Override
    protected void installListener() {
        super.installListener();
        linearLayoutChargebackCause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "弹框", Toast.LENGTH_SHORT).show();
                List<String> list = new ArrayList<>();
//                Collections.addAll(list, chargeBackCause);
                for( int i = 0 ; i  < chargeBackCause.length ; i++) {
                    list.add(chargeBackCause[i]);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChargeBackActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
                ListView listView = new ListView(ChargeBackActivity.this);
                listView.setAdapter(arrayAdapter);

                PopupWindow popupWindow = new PopupWindow(ChargeBackActivity.this);
                popupWindow.setContentView(listView);
                popupWindow.showAsDropDown(v);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        editText2.setText(chargeBackCause[position]);
                    }
                });
            }
        });

        buttonCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "提交申请", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @OnClick({R.id.editText2, R.id.button_commit, R.id.linearLayout_chargeback_cause})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.linearLayout_chargeback_cause:
//
//                break;
//            case R.id.button_commit:
//                break;
//        }
//    }

    private static final String[] chargeBackCause = {"不喜欢／不想要", "未按约定时间发货", "快递／物流未送到", "快递／物流无跟踪记录"};

}
