package com.taihuoniao.fineix.personal.alliance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.alliance.bean.BankListBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/3/9 10:00
 * Email: 895745843@qq.com
 */

public class WithdrawAllianceBankListActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.listView_backlist)
    ListView listViewBacklist;

    public WithdrawAllianceBankListActivity() {
        super(R.layout.activity_alliance_bank_list);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "发卡银行");
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void requestNet() {
        requestDat2a();
    }

    private List<BankListBean.BanksEntity> banksEntities;

    /**
     * 获取银行列表
     */
    private void requestDat2a() {
        HttpRequest.post(URL.ALLIANCE_PAYMENT_GATEWAY_BUNK_OPTIONS, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                BankListBean bankListBean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<BankListBean>>() {
                });
                if (bankListBean != null) {
                    dealResult(bankListBean);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showSuccess("请求错误");
            }
        });
    }

    private void dealResult(BankListBean bankListBean) {
        banksEntities = bankListBean.getBanks();
        int size = banksEntities.size();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(banksEntities.get(i).getName());
        }
        listViewBacklist.setAdapter(new ArrayAdapter<String>(this, R.layout.dialog_baselist_item, list));
        listViewBacklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BankListBean.BanksEntity banksEntity = banksEntities.get(i);
                Intent intent = new Intent();
                intent.putExtra("banksEntity", banksEntity);
                setResult(Activity.RESULT_OK, intent);
                WithdrawAllianceBankListActivity.this.finish();
            }
        });
    }
}
