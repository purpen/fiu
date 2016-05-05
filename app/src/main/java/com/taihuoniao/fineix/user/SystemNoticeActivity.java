package com.taihuoniao.fineix.user;

import android.text.TextUtils;
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
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

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
    private WaittingDialog dialog;

    public SystemNoticeActivity() {
        super(R.layout.activity_system_notice);
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(this);
        custom_head.setHeadCenterTxtShow(true, "系统通知");
        custom_head.setHeadRightTxtShow(true,R.string.clear);
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
                    SystemNoticeData noticeData = JsonUtil.fromJson(responseInfo.result, SystemNoticeData.class);
                    if (noticeData.isSuccess()){
                        ArrayList<SystemNoticeData.SystemNoticeItem> rows = noticeData.rows;
                        refreshUI(rows);
                        return;
                    }
                    Util.makeToast(noticeData.getMessage());
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
    protected void refreshUI(List list) {
        if (list==null) return;
        if (list.size()==0){
            Util.makeToast("暂无数据！");
            return;
        }

        if (adapter==null){
            adapter=new SystemNoticeAdapter(list,this);
            lv.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }
}
