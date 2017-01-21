package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.PrivateMessageListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.PrivateMessageListData;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 * created at 2016/5/6 19:23
 */
public class PrivateMessageListActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.lv)
    ListView lv;
    private static final String PAGE_SIZE="9999";
    private static final String TYPE_ALL="0"; //与全部用户私信列表
    private WaittingDialog dialog;
    private PrivateMessageListAdapter adapter;
    private List<PrivateMessageListData.RowItem> mList=new ArrayList<>();
    public PrivateMessageListActivity(){
        super(R.layout.activity_private_message_list);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"私信");
        dialog=new WaittingDialog(this);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void installListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, PrivateMessageActivity.class);
                if (mList==null) return;
                if (mList.size()<=i) return;
                User user = new User();
                user._id=mList.get(i).users.from_user.id;
                user.nickname=mList.get(i).users.from_user.nickname;
                user.avatar=mList.get(i).users.from_user.big_avatar_url;
                intent.putExtra(UserCenterActivity.class.getSimpleName(),user);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mList.clear();
        requestNet();
    }

    @Override
    protected void requestNet() {
        int curPage = 1;
        HashMap<String, String> params = ClientDiscoverAPI.getgetPrivateMessageListRequestParams(String.valueOf(curPage), PAGE_SIZE, TYPE_ALL);
        HttpRequest.post(params,  URL.MESSAGE_RECORD, new GlobalDataCallBack(){
//        ClientDiscoverAPI.getPrivateMessageList(String.valueOf(curPage), PAGE_SIZE, TYPE_ALL, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (!activity.isFinishing()&&dialog!=null) dialog.show();
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
                HttpResponse<PrivateMessageListData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<PrivateMessageListData>>() {});
                if (response.isSuccess()){
                    List<PrivateMessageListData.RowItem> list = response.getData().rows;
                    refreshUI(list);
                    return;
                }
                ToastUtils.showError(response.getMessage());
//                dialog.showErrorWithStatus(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError("网络异常，请确认网络畅通");
//                dialog.showErrorWithStatus("网络异常，请确认网络畅通");
            }
        });
    }

    @Override
    protected void refreshUI(List list) {
        if (list==null) return;
        if (list.size()==0){
//            Util.makeToast("暂无数据");
            return;
        }

        if (adapter==null){
            mList.addAll(list);
            adapter=new PrivateMessageListAdapter(mList,activity);
            lv.setAdapter(adapter);
        }else {
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }
}
