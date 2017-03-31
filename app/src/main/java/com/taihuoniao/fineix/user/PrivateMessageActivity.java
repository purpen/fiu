package com.taihuoniao.fineix.user;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.PrivateMessageItemAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.MessageDetailData;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;
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
    @Bind(R.id.send_box)
    LinearLayout send_box;
    private boolean isSendMsg=false;
    private int curPage=1;
    private static final String PAGE_SIZE="10";
    private static final String TYPE_USER="1"; //与某个用户的记录
    private User user; //对方
    private WaittingDialog dialog;
    private List<MessageDetailData.MessageItem> mList =new ArrayList<>();
    private PrivateMessageItemAdapter adapter;
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
        lv.requestFocus();
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
        WindowUtils.chenjin(this);
    }

    @Override
    protected void requestNet() {
        if (user==null) return;
        if (user._id<=0) return;
        HashMap<String, String> params =ClientDiscoverAPI. getmessageDetailListRequestParams(String.valueOf(user._id));
        HttpRequest.post(params,  URL.MESSAGE_DETAIL, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                if (dialog!=null&&!isSendMsg) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                if (TextUtils.isEmpty(json)) return;
                HttpResponse<MessageDetailData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<MessageDetailData>>() {
                });
                if (response.isSuccess()){
                    if (isSendMsg){
                        et.requestFocus();
                    }
                    MessageDetailData data = response.getData();
                    List<MessageDetailData.MessageItem> list = data.mailbox;
                    refreshUI(list);
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                Util.makeToast(error);
            }
        });
    }

    @OnClick({R.id.btn})
    void performClick(View v){
        switch (v.getId()){
            case R.id.btn:
                if (user._id== LoginInfo.getUserId()){
                    Util.makeToast("您不能跟自己聊天！");
                    return;
                }
                sendMessage(v);
                break;
        }
    }

    private void sendMessage(final View v){
        final String content=et.getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            Util.makeToast("私信内容不能为空哦！");
            return;
        }
        HashMap<String, String> params = ClientDiscoverAPI.getsendMessageRequestParams(String.valueOf(user._id), content);
        HttpRequest.post(params,  URL.SEND_MESSAGE, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                v.setEnabled(false);
            }

            @Override
            public void onSuccess(String json) {
                isSendMsg=true;
                v.setEnabled(true);
                if (TextUtils.isEmpty(json)) return;
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()){
                    et.getText().clear();
                    ((Button)v).setText("发送");
                    mList.clear();
                    requestNet();
                    return;
                }
                ((Button)v).setText("重新发送");
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                v.setEnabled(true);
                Util.makeToast("请您检查网络！");
            }
        });
    }

    @Override
    protected void refreshUI(List list) {
        if (list==null) return;
        if (list.size()==0){
            Util.makeToast("暂无数据");
            return;
        }

        if (adapter==null){
            mList.addAll(list);
            adapter=new PrivateMessageItemAdapter(mList,activity,user);
            lv.setAdapter(adapter);
        }else {
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }
        lv.setSelection(list.size());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            send_box.requestFocus();
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof LinearLayout)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
