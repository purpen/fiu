package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/5/6 17:03
 */
public class PrivateMessageActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.lv)
    ListView lv;
    @Bind(R.id.et)
    EditText et;
    private int curPage=1;
    private static final String PAGE_SIZE="10";
    private static final String TYPE_USER="1"; //与某个用户的记录
    private User user;
    private WaittingDialog dialog;
    public PrivateMessageActivity(){
        super(R.layout.activity_private_message);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(UserCenterActivity.class.getSimpleName())){
            user=(User)intent.getSerializableExtra(UserCenterActivity.class.getSimpleName());
        }
    }

    @Override
    protected void initView() {
        dialog=new WaittingDialog(this);
        if (user!=null){
            if (TextUtils.isEmpty(user.nickname)){
                if (!TextUtils.isEmpty(user.phone)){
                    custom_head.setHeadCenterTxtShow(true,user.phone);
                }else {
                    custom_head.setHeadCenterTxtShow(true,"对方");
                }
            }else {
                custom_head.setHeadCenterTxtShow(true,user.nickname);
            }
        }
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.messageDetailList(String.valueOf(curPage),new RequestCallBack<String>() {
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

    @OnClick({R.id.btn})
    void performClick(View v){
        switch (v.getId()){
            case R.id.btn:
                v.setEnabled(false);
                if (user._id== LoginInfo.getUserId()){
                    Util.makeToast("您不能跟自己聊天！");
                    return;
                }
                sendMessage(v);
                break;
        }
    }

    private void sendMessage(final View v){
        String content=et.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Util.makeToast("私信内容不能为空哦！");
            return;
        }
        ClientDiscoverAPI.sendMessage(String.valueOf(user._id), content, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog!=null) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                v.setEnabled(true);
                dialog.dismiss();
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                //TODO

            }

            @Override
            public void onFailure(HttpException e, String s) {
                v.setEnabled(true);
                dialog.dismiss();
                Util.makeToast(s);
            }
        });
    }

    @Override
    protected void refreshUI(List list) {

    }
}
