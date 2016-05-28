package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SystemNoticeAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SystemNoticeData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.ArrayList;

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
    private int curPage = 1;
    private SVProgressHUD dialog;
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
        dialog = new SVProgressHUD(this);
        custom_head.setHeadCenterTxtShow(true, "系统通知");
//        custom_head.setHeadRightTxtShow(true,R.string.clear);
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
                    Intent intent=null;
                    switch (item.evt){
                        case 0://链接
                            Uri uri = Uri.parse(item.url);
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            break;
                        case 1: //情景
                            intent=new Intent(activity, QingjingDetailActivity.class);
                            intent.putExtra("id",item.url);
                            startActivity(intent);
                            break;
                        case 2: //场景
                            intent=new Intent(activity, SceneDetailActivity.class);
                            intent.putExtra("id",item.url);
                            startActivity(intent);
                            break;
                        case 3: //产品
                            intent=new Intent(activity, GoodsDetailActivity.class);
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
        ClientDiscoverAPI.getSystemNotice(String.valueOf(curPage), PAGE_SIZE, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                try {
                    HttpResponse<SystemNoticeData> response= JsonUtil.json2Bean(responseInfo.result,new TypeToken<HttpResponse<SystemNoticeData>>(){});
                    if (response.isSuccess()){
                        list = response.getData().rows;
                        refreshUI();
                        return;
                    }
                    Util.makeToast(response.getMessage());
                } catch (JsonSyntaxException e) {
                    LogUtil.e(TAG, e.getLocalizedMessage());
                    Util.makeToast("对不起,数据异常");
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                Util.makeToast(s);
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (list==null) return;
        if (list.size()==0){
            Util.makeToast("暂无数据！");
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
