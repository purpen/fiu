package com.taihuoniao.fineix.user;

import android.text.TextUtils;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.PrivateMessageListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

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
    private int curPage=1;
    private static final String PAGE_SIZE="9999";
    private static final String TYPE_ALL="0"; //与全部用户私信列表
    private WaittingDialog dialog;
    private PrivateMessageListAdapter adapter;
    public PrivateMessageListActivity(){
        super(R.layout.activity_private_message_list);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"私信");
        dialog=new WaittingDialog(this);
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.getPrivateMessageList(String.valueOf(curPage), PAGE_SIZE, TYPE_ALL, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog!=null) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                //TODO
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
//            if (){
//
//            }
            return;
        }

        if (adapter==null){
            adapter=new PrivateMessageListAdapter(list,activity);
            lv.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }
}
