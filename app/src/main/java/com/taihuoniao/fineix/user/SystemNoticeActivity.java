package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SystemNoticeAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.SystemNoticeData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/5/5 23:23
 */
public class SystemNoticeActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.lv)
    ListView lv;
    private SystemNoticeAdapter adapter;
    private static final String PAGE_SIZE = "9999";
    private WaittingDialog dialog;
    private ArrayList<SystemNoticeData.SystemNoticeItem> list;
    private int unread_count;
    public SystemNoticeActivity() {
        super(R.layout.activity_system_notice);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(getClass().getSimpleName())){
            unread_count = intent.getIntExtra(getClass().getSimpleName(),0);
        }
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(this);
        custom_head.setHeadCenterTxtShow(true, "系统通知");
        WindowUtils.chenjin(this);
    }

    @OnClick(R.id.tv_head_right)
    void performClick(View v){
        switch (v.getId()){
            case R.id.tv_head_right:
                if (list==null || list.size()==0) return;
                list.clear();
                adapter.notifyDataSetChanged();
                Util.makeToast("已清空");
                break;
        }
    }

    @Override
    protected void installListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list!=null && list.size()>0){
                    SystemNoticeData.SystemNoticeItem item = list.get(i);
                    Intent intent;
                    switch (item.evt){
                        case 0://链接
                            Uri uri = Uri.parse(item.url);
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            break;
                        case 1: //情景
                            intent=new Intent(activity, QJDetailActivity.class);
                            intent.putExtra("id",item.url);
                            startActivity(intent);
                            break;
                        case 2: //场景
                            intent=new Intent(activity, QJDetailActivity.class);
                            intent.putExtra("id",item.url);
                            startActivity(intent);
                            break;
                        case 3: //产品
                            intent=new Intent(activity, BuyGoodsDetailsActivity.class);
                            intent.putExtra("id",item.url);
                            startActivity(intent);
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void requestNet() {
        int curPage = 1;
        HashMap<String, String> params =ClientDiscoverAPI. getgetSystemNoticeRequestParams(String.valueOf(curPage), PAGE_SIZE);
        HttpRequest.post(params,  URL.SYSTEM_NOTICE, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                if (!activity.isFinishing()&& dialog!=null) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!activity.isFinishing()&& dialog!=null) dialog.dismiss();
                    }
                }, DataConstants.DIALOG_DELAY);
                if (TextUtils.isEmpty(json)) return;
                try {
                    HttpResponse<SystemNoticeData> response= JsonUtil.json2Bean(json,new TypeToken<HttpResponse<SystemNoticeData>>(){});
                    if (response.isSuccess()){
                        list = response.getData().rows;
                        refreshUI();
                        return;
                    }
                    ToastUtils.showError(response.getMessage());
                } catch (JsonSyntaxException e) {
                    LogUtil.e(TAG, e.getLocalizedMessage());
                    ToastUtils.showError("对不起，数据异常");
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError("网络异常，请确认网络畅通");
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (list==null) return;
        if (list.size()==0){
            return;
        }
        for (int i=0;i<unread_count;i++){
            list.get(i).is_unread=true;
        }
        if (adapter==null){
            adapter=new SystemNoticeAdapter(list,this);
            lv.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }
}
