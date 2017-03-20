package com.taihuoniao.fineix.personal.alliance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalCallBack;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.alliance.bean.WithDrawAccountListBean;
import com.taihuoniao.fineix.personal.alliance.bean.WithDrawSetDefaultAccoutBean;
import com.taihuoniao.fineix.personal.alliance.view.BindAccountInfoLinerLayout;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.BaseDialogList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 绑定提现账户
 * Created by Stephen on 2017/3/9 10:00
 * Email: 895745843@qq.com
 */

public class BindWithdrawAccountActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.linearLayout_alliance_withdraw_account_list)
    LinearLayout linearLayoutAllianceWithdrawAccountList;
    @Bind(R.id.linearLayout_account_add_withdraw_account)
    LinearLayout linearLayoutAccountAddWithdrawAccount;

    private static final int REQUESTCODE_ADD_ACCOUNT            =               10006;
    private List<WithDrawAccountListBean.RowsEntity> rowsEntities;
    private WithDrawAccountListBean.RowsEntity selectedEntity;
    private String selectedID;
    private int selectedIndex = -1;

    public BindWithdrawAccountActivity() {
        super(R.layout.activity_alliance_bind_withdraw_account);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "绑定提现账户");
        customHead.setHeadRightTxtShow(true, "管理");
        customHead.getHeadRightTV().setOnClickListener(this);
        customHead.setGoBackListenr(new CustomHeadView.IgobackLister() {
            @Override
            public void goback() {
                onBackPressed();
            }
        });
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void getIntentData() {
        selectedID = getIntent().getStringExtra("accountId");
    }

    @OnClick({R.id.linearLayout_account_add_withdraw_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_account_add_withdraw_account:
                showBindAccountType();
                break;
            case R.id.tv_head_right:
                if (TextUtils.isEmpty(selectedID)) {
                    ToastUtils.showError("请先选择一个账户");
                } else {
                    showManageCurrentAccount();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUESTCODE_ADD_ACCOUNT) {
            resetSelectedValues();
            getAccountList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 加载绑定账户列表
     */
    private void getAccountList() {
        HashMap<String, String> hashMap = new HashMap<>();
        HttpRequest.post(hashMap, URL.ALLIANCE_PAYMENT_CARD_GETLIST, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                WithDrawAccountListBean withDrawAccountListBean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<WithDrawAccountListBean>>() {});
                if (withDrawAccountListBean != null) {
                    dealResult(withDrawAccountListBean);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("加载失败");
            }
        });
    }

    private void dealResult(WithDrawAccountListBean withDrawAccountListBean) {
        if (withDrawAccountListBean == null || Integer.valueOf(withDrawAccountListBean.getTotal_rows()) < 1) {
            linearLayoutAllianceWithdrawAccountList.removeAllViews();
            return;
        }
        rowsEntities = withDrawAccountListBean.getRows();
        linearLayoutAllianceWithdrawAccountList.removeAllViews();
        for (int i = 0; i < rowsEntities.size(); i++) {
            WithDrawAccountListBean.RowsEntity rowsEntity = rowsEntities.get(i);
            BindAccountInfoLinerLayout linerLayout = new BindAccountInfoLinerLayout(this);
            int valueOf = Integer.valueOf(rowsEntity.getIs_default());
            linerLayout.setInitInfo(Integer.valueOf(rowsEntity.getKind()), rowsEntity.getKind_label(), rowsEntity.getPay_type_label(), rowsEntity.getAccount(), valueOf == 1);
            if (TextUtils.equals(rowsEntity.get_id(), selectedID) || ( selectedID == null && valueOf == 1 && selectedIndex == -1 || selectedIndex == i)) {
                linerLayout.setSelectStatus(true);
//                if (valueOf == 1 && selectedIndex == -1) {
//                    selectedIndex = 0;
//                }
                selectedEntity = rowsEntity;
                selectedID = rowsEntity.get_id();
            } else {
                linerLayout.setSelectStatus(false);
            }

            linerLayout.setOnChangedListener(new GlobalCallBack() {
                @Override
                public void callBack(Object object) {
                    setLinearLayoutSelectStatus((Integer) object);
                }
            }, i);
            linearLayoutAllianceWithdrawAccountList.addView(linerLayout);
        }
    }

    private void setLinearLayoutSelectStatus(int index) {
        int childCount = linearLayoutAllianceWithdrawAccountList.getChildCount();
        for (int i = 0; i < childCount; i++) {
            BindAccountInfoLinerLayout childAt = (BindAccountInfoLinerLayout) linearLayoutAllianceWithdrawAccountList.getChildAt(i);
            if (i == index) {
                childAt.setSelectStatus(true);
                selectedEntity = rowsEntities.get(i);
                selectedID = selectedEntity.get_id();
                selectedIndex = i;
            } else {
                childAt.setSelectStatus(false);
            }
        }
    }

    @Override
    protected void requestNet() {
        getAccountList();
    }

    /**
     * 绑定账户类型
     */
    private void showBindAccountType(){
        final ArrayList<String> customerList = new ArrayList<>();
        Collections.addAll(customerList, strings);
        BaseDialogList dialogList = new BaseDialogList(this, new BaseDialogList.SubmitListener() {
            @Override
            public void submit(int position) {
                switch (position) {
                    case 0:
                        createOrmodifyAccountInformation("1", null);
                        break;
                    case 1:
                        createOrmodifyAccountInformation("2", null);
                        break;
                }
            }
        }, "请选择绑定账户", Color.parseColor("#222222"));
        dialogList.setContent(customerList);
        dialogList.show();
    }

    private String[] strings = new String[]{"绑定银行卡","绑定支付宝"};

    private void showManageCurrentAccount(){
        final ArrayList<String> customerList = new ArrayList<>();
        Collections.addAll(customerList, strings2);
        BaseDialogList dialogList = new BaseDialogList(this, new BaseDialogList.SubmitListener() {
            @Override
            public void submit(int position) {
                switch (position) {
                    case 0:
                        bindAccount(selectedID);
                        break;
                    case 1:
                        if (selectedEntity != null) {
                            createOrmodifyAccountInformation(selectedEntity.getKind(), selectedEntity);
                        }
                        break;
                    case 2:
                        unBindAccount(selectedID);
                        break;
                }
            }
        }, "");
        dialogList.setContent(customerList);
        dialogList.show();
    }
    private String[] strings2 = new String[]{"设为默认提现账户","修改账户","删除"};

    /**
     * 设为默认账户
     */
    private void bindAccount(String id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        HttpRequest.post(hashMap, URL.ALLIANCE_PAYMENT_SET_DEFAULT, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<WithDrawSetDefaultAccoutBean> beanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<WithDrawSetDefaultAccoutBean>>(){});
                if (beanHttpResponse != null && beanHttpResponse.isSuccess()) {
                    resetSelectedValues();
                    getAccountList();
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("请求失败");
            }
        });
    }

    /**
     * 解绑账户
     */
    private void unBindAccount(String id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        HttpRequest.post(hashMap, URL.ALLIANCE_PAYMENT_DELETE, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<WithDrawSetDefaultAccoutBean> beanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<WithDrawSetDefaultAccoutBean>>(){});
                if (beanHttpResponse != null && beanHttpResponse.isSuccess()) {
                    resetSelectedValues();
                    getAccountList();
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("解绑失败");
            }
        });
    }

    /**
     * 创建/修改账户信息
     * @param kind // 账号类型：1.银行卡；2.支付宝；3.--
     * @param bean
     */
    private void createOrmodifyAccountInformation(String kind, WithDrawAccountListBean.RowsEntity bean) {
        switch (kind) {
            case "1":
                Intent intent2 = new Intent(BindWithdrawAccountActivity.this, AddWithdrawAccountInfo2Activity.class);
                intent2.putExtra("ParcelableExtraRowsEntity", bean);
                startActivityForResult(intent2, REQUESTCODE_ADD_ACCOUNT);
                break;
            case "2":
                Intent intent = new Intent(BindWithdrawAccountActivity.this, AddWithdrawAccountInfoActivity.class);
                intent.putExtra("ParcelableExtraRowsEntity", bean);
                startActivityForResult(intent, REQUESTCODE_ADD_ACCOUNT);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("id", selectedID);
        setResult(Activity.RESULT_OK, intent);
        BindWithdrawAccountActivity.this.finish();
    }

    private void resetSelectedValues() {
        selectedID = null;
        selectedIndex = -1;
        selectedEntity = null;
    }
}
