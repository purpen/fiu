package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.NoticeAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.NoticeData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 * created at 2016/5/10 16:46
 */
public class NoticeActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.lv)
    ListView lv;
    private int curPage=1;
    private List<NoticeData.NoticeItem> list;
    private static final String pageSize="9999";
    private static final String COMMENT_TYPE="12";
    private WaittingDialog dialog;
    private NoticeAdapter adapter;


    public NoticeActivity(){
        super(R.layout.activity_notice);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"提醒");
        dialog=new WaittingDialog(this);
    }

    @Override
    protected void installListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NoticeData.NoticeItem item=list.get(i);
                Intent intent =null;
                if (item.kind==12){ //情景
                    intent = new Intent(activity, QingjingDetailActivity.class);
                    intent.putExtra("id",item.related_id);
                }else {
                    intent = new Intent(activity, SceneDetailActivity.class);
                    intent.putExtra("id",item.related_id);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void requestNet() {
        String type="1"; //Fiu
        ClientDiscoverAPI.getNoticeList(String.valueOf(curPage),pageSize,type, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                if (dialog!=null) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e(TAG,responseInfo.result);
                HttpResponse<NoticeData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<NoticeData>>() {
                });
                if (response.isSuccess()){
                    list=response.getData().rows;
                    refreshUI();
                }else {
                    Util.makeToast(response.getMessage());
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                if (TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (list==null) return;
        if (list.size()==0) {
            Util.makeToast("暂无提醒");
            return;
        }
        if (adapter==null){
            adapter=new NoticeAdapter(list,activity);
            lv.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }
}
