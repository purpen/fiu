package com.taihuoniao.fineix.personal.alliance;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.AllianceRequstDeal;
import com.taihuoniao.fineix.personal.alliance.bean.BankListBean;
import com.taihuoniao.fineix.personal.alliance.bean.WithDrawAccountListBean;
import com.taihuoniao.fineix.personal.alliance.bean.WithDrawCreateAccountBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.BaseDialogList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Stephen on 2017/3/9 10:00
 * Email: 895745843@qq.com
 */

public class AddWithdrawAccountInfo2Activity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.editText1)
    EditText editText1;
    @Bind(R.id.editText2)
    EditText editText2;
    @Bind(R.id.editText3)
    EditText editText3;
    @Bind(R.id.editText7)
    EditText editText7;
    @Bind(R.id.editText4)
    EditText editText4;
    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.checkBox1)
    CheckBox checkBox1;
    @Bind(R.id.textView_alliance_select_bank)
    TextView textViewAllianceSelectBank;

    private List<BankListBean.BanksEntity> banksEntities;
    private WithDrawAccountListBean.RowsEntity rowsEntity;
    private String bankId;
    private boolean isDefault;

    public AddWithdrawAccountInfo2Activity() {
        super(R.layout.activity_alliance_add_withdraw_account_info2);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "绑定银行卡");
        customHead.setHeadRightTxtShow(true, "保存");
        customHead.getHeadRightTV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitCardInfomation();
            }
        });
        WindowUtils.chenjin(this);
    }

    private void commitCardInfomation() {
        String str = editText1.getText().toString();
        String str1 = editText2.getText().toString();
        String str2 = editText3.getText().toString();
        String str3 = editText7.getText().toString();
        String str4 = editText4.getText().toString();

        if (TextUtils.isEmpty(bankId)) {
            ToastUtils.showError("请选择银行");
        } else if (TextUtils.isEmpty(str)) {
            ToastUtils.showError("开户行不能为空");
        } else if (TextUtils.isEmpty(str1)) {
            ToastUtils.showError("持卡人姓名不能为空");
        } else if (TextUtils.isEmpty(str2)) {
            ToastUtils.showError("请输入银行卡号");
        } else if (TextUtils.isEmpty(str3)) {
            ToastUtils.showError("请输入手机号");
        } else if (TextUtils.isEmpty(str4)) {
            ToastUtils.showError("请输入短信验证码");
        } else {
            if (rowsEntity != null) {
                createBankInfomation(rowsEntity.get_id(),AllianceRequstDeal.getAllianceValue(),"1",bankId, str2, str,isDefault ? "1" : "0", str1,str3, str4);
            } else {
                createBankInfomation(null,AllianceRequstDeal.getAllianceValue(),"1",bankId, str2, str,isDefault ? "1" : "0", str1,str3, str4);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        rowsEntity = getIntent().getParcelableExtra("rowsEntity");
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isDefault = b;
            }
        });
    }

    /**
     * 修改创建银行卡信息
     */
    private void createBankInfomation(String id,String alliance_id,String kind,String pay_type,String account,String bank_address ,String is_default,String username,String phone,String verify_code) {
        Map<String, String> allianceWithDraw01 = ClientDiscoverAPI.getAllianceWithDraw01(id, alliance_id, kind, pay_type, account, bank_address, is_default, username, phone, verify_code);
        HttpRequest.post(allianceWithDraw01, URL.ALLIANCE_PAYMENT_CARD_SAVE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                WithDrawCreateAccountBean bean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<WithDrawCreateAccountBean>>() {
                });
                if (bean != null) {
                    Toast.makeText(activity, "添加信息成功", Toast.LENGTH_SHORT).show();
                    AddWithdrawAccountInfo2Activity.this.finish();
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("提现失败");
            }
        });
    }

    @OnClick(R.id.textView_alliance_select_bank)
    public void onClick() {
        showSelectedBankListDialog();
    }

    private String[] strings2 = new String[] {"中国银行","农业银行", "交通银行", "北京银行"};

    /**
     * 修改创建银行卡信息
     */
    private void requestDat2a() {
        HttpRequest.post(URL.ALLIANCE_PAYMENT_GATEWAY_BUNK_OPTIONS, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                BankListBean bankListBean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<BankListBean>>() { });
                if (bankListBean != null) {
                    dealResult(bankListBean);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("提现失败");
            }
        });
    }
    private void dealResult(BankListBean bankListBean) {
        banksEntities = bankListBean.getBanks();
        int size = banksEntities.size();
        strings2 = new String[size];
        for(int i = 0; i < size; i++) {
            strings2[i] = banksEntities.get(i).getName();
        }
    }

    private void showSelectedBankListDialog(){
        final ArrayList<String> customerList = new ArrayList<>();
        Collections.addAll(customerList, strings2);
        BaseDialogList dialogList = new BaseDialogList(this, new BaseDialogList.SubmitListener() {
            @Override
            public void submit(int position) {
                BankListBean.BanksEntity banksEntity = banksEntities.get(position);
                textViewAllianceSelectBank.setText(customerList.get(position));
                bankId = banksEntity.getId();
            }
        }, "");
        dialogList.setContent(customerList);
        dialogList.show();
    }

    @Override
    protected void requestNet() {
        requestDat2a();
    }
}
