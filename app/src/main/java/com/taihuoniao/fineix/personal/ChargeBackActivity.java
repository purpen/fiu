package com.taihuoniao.fineix.personal;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

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

    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.editText1)
    EditText editText1;
    @Bind(R.id.editText2)
    EditText editText2;
    @Bind(R.id.editText3)
    EditText editText3;
    @Bind(R.id.button_commit)
    Button buttonCommit;

    private static final String TITLE = "退款";

    public ChargeBackActivity() {
        super(R.layout.activity_chargeback);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, TITLE);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.editText2, R.id.button_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editText2:
                final List<String> list = new ArrayList<>();
                Collections.addAll(list, chargeBackCause);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChargeBackActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1,list );
                ListView listView = new ListView(ChargeBackActivity.this);
                listView.setAdapter(arrayAdapter);

                PopupWindow popupWindow = new PopupWindow(ChargeBackActivity.this);
                popupWindow.setContentView(listView);
                popupWindow.showAsDropDown(view);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((EditText)view).setText(list.get(position));
                    }
                });
                break;
            case R.id.button_commit:
                break;
        }
    }

    private static final String[]  chargeBackCause = {"不喜欢／不想要", "未按约定时间发货", "快递／物流未送到", "快递／物流无跟踪记录"};

}
