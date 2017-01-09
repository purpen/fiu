package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.NoticeAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.NoticeBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.CommentListActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/5/10 16:46
 */
public class NoticeActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.lv)
    ListView lv;
    private List<NoticeBean.DataBean.RowsBean> list;
    private static final String pageSize = "9999";
    private static final String COMMENT_TYPE = "12";
    private WaittingDialog dialog;
    private NoticeAdapter adapter;


    public NoticeActivity() {
        super(R.layout.activity_notice);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "提醒");
        dialog = new WaittingDialog(this);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void installListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NoticeBean.DataBean.RowsBean item = list.get(i);
                Intent intent = new Intent();
                if (item.getKind() == 12) { //情景
                    intent.setClass(activity,QJDetailActivity.class);
                    intent.putExtra("id", item.getTarget_obj().get_id());
                } else if (item.getKind() == 3) {//评论
//                    ToastUtils.showError("跳转评论页面缺少参数");
                    intent.setClass(activity, CommentListActivity.class);
                    intent.putExtra("target_id", list.get(i).getComment_target_obj().get_id());
//                    intent.putExtra("target_user_id", list.get(i).getUser().get_id());
                    intent.putExtra("type", 12 + "");
                    intent.putExtra(UserCommentsActivity.class.getSimpleName(), list.get(i).getSend_user().getNickname());
                    intent.putExtra("reply_id", list.get(i).getTarget_obj().get_id());
                    intent.putExtra("reply_user_id", list.get(i).getSend_user().get_id());
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void requestNet() {
        String type = "1"; //Fiu
//        app_type=2, channel=10, client_id=1415289600, page=1, size=9999, time=1474441155, type=1, uuid=ffffffff-b056-1c0b-ffff-ffffa8556b0e, sign=fd2a1e20ff4344c098ac08c98d3b9c22
        int curPage = 1;
        RequestParams params = ClientDiscoverAPI.getgetNoticeListRequestParams(String.valueOf(curPage), pageSize, type);
        HttpRequest.post(params,     URL.NOTICE_LIST, new GlobalDataCallBack(){
//        ClientDiscoverAPI.getNoticeList(String.valueOf(curPage), pageSize, type, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                if (dialog != null) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                Log.e("<<<提醒", json);
                dialog.dismiss();
                NoticeBean noticeBean = new NoticeBean();
                try {
                    Gson gson = new Gson();
                    Type type1 = new TypeToken<NoticeBean>() {
                    }.getType();
                    noticeBean = gson.fromJson(json, type1);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<提醒列表", "解析异常=" + e.toString());
                }
                if (noticeBean.isSuccess()) {
                    list = noticeBean.getData().getRows();
                    if (adapter == null) {
                        adapter = new NoticeAdapter(list, activity);
                        lv.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showError(noticeBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (list == null) return;
        if (list.size() == 0) {
            return;
        }
        if (adapter == null) {
            adapter = new NoticeAdapter(list, activity);
            lv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


}
