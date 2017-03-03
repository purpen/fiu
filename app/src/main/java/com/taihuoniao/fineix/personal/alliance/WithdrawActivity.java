package com.taihuoniao.fineix.personal.alliance;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.AllianceRequstDeal;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.DefaultDialog;
import com.taihuoniao.fineix.view.dialog.IDialogListenerConfirmBack;

import java.util.HashMap;

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

    private String balance;
    private String amount;

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

//        editText1.setSelection(editText1.length());
        editText1.addTextChangedListener(new MyTextWatcher());
        initUI();
    }

    private void initUI() {
        balance = getIntent().getStringExtra("balance");
        textView2.setText(String.format("¥ %s", balance));
    }

    @OnClick({R.id.textView_link1, R.id.button_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_link1:
                amount = balance;
                editText1.setText(amount);
                editText1.setSelection(editText1.length());
                break;
            case R.id.button_commit:
                new DefaultDialog(this, String.format("¥ %s", amount), "请确认要提现的金额", "提现", new IDialogListenerConfirmBack() {
                    @Override
                    public void clickRight() {
                        requestData(amount);
                    }
                });
                break;
        }
    }

    private void requestData(String amount) {
        String allianceValue = AllianceRequstDeal.getAllianceValue();
        HashMap<String, String> hashMap = ClientDiscoverAPI.getWithdraw_cash(allianceValue, amount);
        HttpRequest.post(hashMap, URL.ALLIANCE_BALANCE_WITHDRAW_CASH_APPLY_CASH, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse httpResponse = JsonUtil.fromJson(json, HttpResponse.class);
                if (httpResponse.isSuccess()) {

                    // TODO: 2017/1/19 提现成功
                    startActivity(new Intent(WithdrawActivity.this, MyAccountActivity.class));
                    WithdrawActivity.this.finish();
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("提现失败");
            }
        });
    }

    class MyTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            LogUtil.e("s===="+s.length()+"===="+s.toString());
            if (TextUtils.isEmpty(s) || ".".equals(s) || "0".equals(s)) {
                textView1.setVisibility(View.VISIBLE);
                buttonCommit.setEnabled(false);
            } else if (s.length() < 2) {
                textView1.setVisibility(View.VISIBLE);
                buttonCommit.setEnabled(false);
            } else if (Double.valueOf(s.toString()) < 10.00D) {
                textView1.setVisibility(View.VISIBLE);
                buttonCommit.setEnabled(false);
            } else if (Double.valueOf(s.toString()) > Double.valueOf(balance)) {
                editText1.setText(balance);
                editText1.setSelection(editText1.length());
                textView1.setVisibility(View.INVISIBLE);
                buttonCommit.setEnabled(true);
            } else {
                textView1.setVisibility(View.INVISIBLE);
                buttonCommit.setEnabled(true);
            }
            amount = editText1.getText().toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
