package com.taihuoniao.fineix.personal.alliance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.AllianceRequstDeal;
import com.taihuoniao.fineix.personal.alliance.bean.WithDrawDefaultAccountBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.StringFormatUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.DefaultDialog;
import com.taihuoniao.fineix.view.dialog.IDialogListenerConfirmBack;

import java.util.HashMap;
import java.util.Map;

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
    @Bind(R.id.imageView_alliance_withdraw_account_icon)
    ImageView imageViewAllianceWithdrawAccountIcon;
    @Bind(R.id.textView_alliance_withdraw_account_description)
    TextView textViewAllianceWithdrawAccountDescription;
    @Bind(R.id.linearLayout_alliance_withdraw_account)
    LinearLayout linearLayoutAllianceWithdrawAccount;
    @Bind(R.id.linearLayout_alliance_noBindAccount)
    LinearLayout linearLayoutAllianceNoBindAccount;
    @Bind(R.id.textView_alliance_withdraw_account_type)
    TextView textViewAllianceWithdrawAccountType;
    @Bind(R.id.linearLayout_alliance_BindAccount)
    LinearLayout linearLayoutAllianceBindAccount;

    private static int REQUESTCODE_SELECTED_ACCOUNT = 10002;
    private String balance;
    private String amount;
    private String accountId;

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
        ButterKnife.bind(this);
        editText1.addTextChangedListener(new MyTextWatcher());
        initUI();
    }

    private void initUI() {
        balance = getIntent().getStringExtra("balance");
        if (!TextUtils.isEmpty(balance)) {
            balance = StringFormatUtils.formatMoney(Double.valueOf(balance));
        }
        textView2.setText(String.format("¥ %s", balance));
    }

    @OnClick({R.id.textView_link1, R.id.button_commit, R.id.linearLayout_alliance_withdraw_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_link1:
                amount = balance;
                editText1.setText(amount);
                editText1.setSelection(editText1.length());
                break;
            case R.id.button_commit:
                if (TextUtils.isEmpty(accountId)) {
                    ToastUtils.showError("请先绑定要提现的账户");
                    return;
                }
                new DefaultDialog(this, String.format("¥ %s", amount), "请确认要提现的金额", "提现", new IDialogListenerConfirmBack() {
                    @Override
                    public void clickRight() {
                        requestData(amount, accountId);
                    }
                });
                break;
            case R.id.linearLayout_alliance_withdraw_account:
                Intent intent = new Intent(WithdrawActivity.this, BindWithdrawAccountActivity.class);
                intent.putExtra("accountId", accountId);
                startActivityForResult(intent, REQUESTCODE_SELECTED_ACCOUNT);
                break;
        }
    }

    private void requestData(String amount, String accountId) {
        String allianceValue = AllianceRequstDeal.getAllianceValue();
        HashMap<String, String> hashMap = ClientDiscoverAPI.getWithdraw_cash(allianceValue, amount, accountId);
        HttpRequest.post(hashMap, URL.ALLIANCE_BALANCE_WITHDRAW_CASH_APPLY_CASH, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse httpResponse = JsonUtil.fromJson(json, HttpResponse.class);
                if (httpResponse.isSuccess()) {
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

    class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(accountId)) {
                ToastUtils.showError("请先绑定要提现的账户");
                return;
            } else if (TextUtils.isEmpty(s) || ".".equals(s) || "0".equals(s)) {
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

    /**
     * 获取默认绑定接口
     */
    private void requestData2() {
        Map<String, String> allianceWithDraw04 = ClientDiscoverAPI.getAllianceWithDraw04();
        HttpRequest.post(allianceWithDraw04, URL.ALLIANCE_PAYMENT_CARD_DEFAULTED, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                WithDrawDefaultAccountBean withDrawDefaultAccountBean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<WithDrawDefaultAccountBean>>() {
                });
                if (withDrawDefaultAccountBean != null) {
                    dealResult(withDrawDefaultAccountBean);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("请求失败");
            }
        });
    }

    private void dealResult(WithDrawDefaultAccountBean withDrawDefaultAccountBean) {
        if (withDrawDefaultAccountBean == null) {
            return;
        }
        String has_default = withDrawDefaultAccountBean.getHas_default();
        if (Integer.valueOf(has_default) == 1) {
            loadSeletedAccountInfo(withDrawDefaultAccountBean);
        } else {
            linearLayoutAllianceNoBindAccount.setVisibility(View.VISIBLE);
            linearLayoutAllianceBindAccount.setVisibility(View.GONE);
        }
    }

    private void loadSeletedAccountInfo(WithDrawDefaultAccountBean withDrawDefaultAccountBean) {
        String kind = withDrawDefaultAccountBean.getKind();
        if (TextUtils.isEmpty(kind)) {
            return;
        }
        String account = withDrawDefaultAccountBean.getAccount();
        if (Integer.valueOf(kind) == 1) { //银行卡
            imageViewAllianceWithdrawAccountIcon.setImageResource(R.mipmap.icon_account_bank);
            String pay_type_label = withDrawDefaultAccountBean.getPay_type_label();
            textViewAllianceWithdrawAccountType.setText(pay_type_label);
            textViewAllianceWithdrawAccountType.setTextColor(Color.parseColor("#222222"));
            textViewAllianceWithdrawAccountDescription.setText("储蓄卡(尾号 " + account.substring(account.length() - 4) + ")");
        } else if (Integer.valueOf(kind) == 2) { //支付宝
            imageViewAllianceWithdrawAccountIcon.setImageResource(R.mipmap.icon_account_alipay);
            textViewAllianceWithdrawAccountType.setText("支付宝");
            textViewAllianceWithdrawAccountType.setTextColor(Color.parseColor("#10AEFF"));
            textViewAllianceWithdrawAccountDescription.setText("支付宝(" + StringFormatUtils.formatAccountNumber(account) + ")");
        }
        linearLayoutAllianceBindAccount.setVisibility(View.VISIBLE);
        linearLayoutAllianceNoBindAccount.setVisibility(View.GONE);
        accountId = withDrawDefaultAccountBean.get_id();
    }

    @Override
    protected void requestNet() {
        requestData2();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_SELECTED_ACCOUNT && data != null) {
            if (data.getStringExtra("id") != null) {
                requestData3(data.getStringExtra("id"));
            } else {
                accountId = null;
                requestData2();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取提现账户详情接口
     */
    private void requestData3(String id) {
        Map<String, String> allianceWithDraw04 = ClientDiscoverAPI.getDefaultParams();
        allianceWithDraw04.put("id", id);
        HttpRequest.post(allianceWithDraw04, URL.ALLIANCE_BALANCE_CARD_VIEW, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                WithDrawDefaultAccountBean withDrawDefaultAccountBean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<WithDrawDefaultAccountBean>>() {
                });
                if (withDrawDefaultAccountBean != null) {
                    loadSeletedAccountInfo(withDrawDefaultAccountBean);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("请求失败");
            }
        });
    }
}
