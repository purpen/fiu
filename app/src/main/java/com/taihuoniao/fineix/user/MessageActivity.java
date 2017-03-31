package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomItemLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/4/28 19:15
 */
public class MessageActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.item_push_setting)
    CustomItemLayout item_push_setting;
    @Bind(R.id.item_clear_cache)
    CustomItemLayout item_clear_cache;
    @Bind(R.id.item_to_comment)
    CustomItemLayout item_to_comment;
    @Bind(R.id.item_notice)
    CustomItemLayout item_notice;
    @Bind(R.id.item_fans)
    CustomItemLayout itemFans;
    private WaittingDialog dialog;
    private User user;

    public MessageActivity() {
        super(R.layout.activity_message);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        requestNet();
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "消息");
        dialog = new WaittingDialog(this);
        item_push_setting.setTVStyle(R.mipmap.sys_msg, "系统通知", R.color.color_333);
        item_clear_cache.setTVStyle(R.mipmap.icon_comment, "评论", R.color.color_333);
        item_to_comment.setTVStyle(R.mipmap.private_msg, "私信", R.color.color_333);
        item_notice.setTVStyle(R.mipmap.notice, "提醒", R.color.color_333);
        itemFans.setTVStyle(R.mipmap.message_fans, "粉丝", R.color.color_333);
        WindowUtils.chenjin(this);
    }

    @OnClick({R.id.item_push_setting, R.id.item_clear_cache, R.id.item_to_comment, R.id.item_notice, R.id.item_fans})
    void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.item_push_setting: //系统通知
                intent = new Intent(activity, SystemNoticeActivity.class);
                if (user != null) {
                    intent.putExtra(SystemNoticeActivity.class.getSimpleName(), user.counter.fiu_notice_count);
                    startActivity(intent);
                }
                break;
            case R.id.item_clear_cache: //评论列表
                intent = new Intent(activity, UserCommentsActivity.class);
                if (user != null) {
                    intent.putExtra(UserCommentsActivity.class.getSimpleName(), user.counter.fiu_comment_count);
                    startActivity(intent);
                }
                break;
            case R.id.item_to_comment: //评论列表
                startActivity(new Intent(activity, PrivateMessageListActivity.class));
                break;
            case R.id.item_notice: //提醒
                startActivity(new Intent(activity, NoticeActivity.class));
                break;
            case R.id.item_fans://粉丝
                Intent intent1 = new Intent(activity, FansActivity.class);
                intent1.putExtra(MessageActivity.class.getSimpleName(), user.counter.fans_count);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void requestNet() {
        HttpRequest.post( URL.USER_CENTER, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                if (!activity.isFinishing() && dialog != null) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                    }
                }, DataConstants.DIALOG_DELAY);
                LogUtil.e("result", json);
                if (TextUtils.isEmpty(json)) {
                    return;
                }
                try {
                    HttpResponse<User> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<User>>() {
                    });
                    if (response.isSuccess()) {
                        user = response.getData();
                        refreshUI();
                    }
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
        if (user == null) {
            return;
        }
        if (user.counter != null) {
            item_push_setting.setTipsNum(user.counter.fiu_notice_count); //系统通知
            item_clear_cache.setTipsNum(user.counter.fiu_comment_count); //评论
            item_to_comment.setTipsNum(user.counter.message_count);   //私信
            item_notice.setTipsNum(user.counter.fiu_alert_count);   //提醒数量
            itemFans.setTipsNum(user.counter.fans_count);//粉丝数量
        }
    }
}
