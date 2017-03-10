package com.taihuoniao.fineix.personal.alliance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
 * 绑定体现账户
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

    private static final int WITHDRAW_APLIPAY = 10004;
    private static final int WITHDRAW_BANK = 10005;
    private static final int REQUESTCODE_ADD_ACCOUNT = 10006;


    private List<LinearLayout> mLinearLayouts;

    private int defaultType;
    private String selectedID;

    public BindWithdrawAccountActivity() {
        super(R.layout.activity_alliance_bind_withdraw_account);
    }

    @Override
    protected void initView() {
        mLinearLayouts = new ArrayList<>();
        customHead.setHeadCenterTxtShow(true, "绑定体现账户");
        customHead.setHeadRightTxtShow(true, "管理");
        customHead.getHeadRightTV().setOnClickListener(this);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
//        textViewSetDefaultAlipay.setOnClickListener(this);
//        textViewSetDefaultBank.setOnClickListener(this);
//        liearLayoutBindAccountAlipay.setOnClickListener(this);
//        liearLayoutBindAccountBank.setOnClickListener(this);
//        linearLayoutAccountAddWithdrawAccount.setOnClickListener(this);
    }



    @OnClick({R.id.linearLayout_account_add_withdraw_account})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.textView_setDefault_alipay:
//                break;
//            case R.id.liearLayout_bind_account_alipay:
//                textViewSetDefaultAlipay.setText("");
//                Drawable drawable = getResources().getDrawable(R.mipmap.icon_account_alipay);
//                textViewSetDefaultAlipay.setCompoundDrawables(null, null, drawable, null);
//                defaultType = WITHDRAW_APLIPAY;
//                break;
//            case R.id.textView_setDefault_bank:
//                break;
//            case R.id.liearLayout_bind_account_bank:
//                Drawable drawable2 = getResources().getDrawable(R.mipmap.icon_account_bank);
//                textViewSetDefaultBank.setCompoundDrawables(null, null, drawable2, null);
//                defaultType = WITHDRAW_BANK;
//                break;
            case R.id.linearLayout_account_add_withdraw_account:
//                showDialog();
                showBindAccountType();
                break;
            case R.id.tv_head_right:
                showManageCurrentAccount();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_ADD_ACCOUNT) {
            // TODO: 2017/3/9 加载绑定账户列表
            requestData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 加载绑定账户列表
     */
    private void requestData() {
        HashMap<String, String> hashMap = new HashMap<>();
        HttpRequest.post(hashMap, URL.ALLIANCE_PAYMENT_CARD_GETLIST, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                WithDrawAccountListBean withDrawAccountListBean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<WithDrawAccountListBean>>() {
                });
                if (withDrawAccountListBean != null) {
                    dealResult(withDrawAccountListBean);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("提现失败");
            }
        });
    }

    private void dealResult(WithDrawAccountListBean withDrawAccountListBean) {
        if (withDrawAccountListBean == null || Integer.valueOf(withDrawAccountListBean.getTotal_rows()) < 1) {
            return;
        }
        List<WithDrawAccountListBean.RowsEntity> rowsEntities = withDrawAccountListBean.getRows();
        linearLayoutAllianceWithdrawAccountList.removeAllViews();
        for (int i = 0; i < rowsEntities.size(); i++) {
            WithDrawAccountListBean.RowsEntity rowsEntity = rowsEntities.get(i);
            BindAccountInfoLinerLayout linerLayout = new BindAccountInfoLinerLayout(this);
            int valueOf = Integer.valueOf(rowsEntity.getIs_default());
            if (valueOf != 1) {
                linerLayout.setInitInfo(Integer.valueOf(rowsEntity.getKind()), rowsEntity.getKind_label(), true, true);
            } else {
                linerLayout.setInitInfo(Integer.valueOf(rowsEntity.getKind()), rowsEntity.getKind_label(), false, false);
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
                continue;
            }
            childAt.setSelectStatus(false);
        }
    }

    @Override
    protected void requestNet() {
        requestData();
    }

    /**
     * 点击添加提现账户
     */
    private void showBindAccountType(){
        final ArrayList<String> customerList = new ArrayList<>();
        Collections.addAll(customerList, strings);
        BaseDialogList dialogList = new BaseDialogList(this, new BaseDialogList.SubmitListener() {
            @Override
            public void submit(int position) {
                switch (position) {
                    case 0:
                        Intent intent2 = new Intent(BindWithdrawAccountActivity.this, AddWithdrawAccountInfo2Activity.class);
                        startActivityForResult(intent2, REQUESTCODE_ADD_ACCOUNT);
                        break;
                    case 1:
                        Intent intent = new Intent(BindWithdrawAccountActivity.this, AddWithdrawAccountInfoActivity.class);
                        startActivityForResult(intent, REQUESTCODE_ADD_ACCOUNT);
                        break;
                }
            }
        }, "请选择绑定账户");
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
                Toast.makeText(activity, customerList.get(position), Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        bindAccount(selectedID);
                        break;
                    case 1:
                        // TODO: 2017/3/10  修改
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
     * 获取绑定账户列表
     */
    private void getAccountList(){
        HashMap<String, String> hashMap = new HashMap<>();
        HttpRequest.post(hashMap, URL.ALLIANCE_PAYMENT_CARD_GETLIST, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                WithDrawAccountListBean withDrawAccountListBean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<WithDrawAccountListBean>>() {
                });
                if (withDrawAccountListBean != null) {
                    dealResult(withDrawAccountListBean);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("提现失败");
            }
        });
    }

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
                    requestData();
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("提现失败");
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
                    requestData();
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("提现失败");
            }
        });
    }
}
