package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.CommentListActivity;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomItemLayout;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/4/28 19:15
 */
public class MessageActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.item_push_setting)
    CustomItemLayout item_push_setting;
    @Bind(R.id.item_clear_cache)
    CustomItemLayout item_clear_cache;
    @Bind(R.id.item_to_comment)
    CustomItemLayout item_to_comment;
//    @Bind(R.id.item_welcome_page)
//    CustomItemLayout item_welcome_page;
    @Bind(R.id.item_feedback)
    CustomItemLayout item_feedback;
    @Bind(R.id.item_about_us)
    CustomItemLayout item_about_us;
    public MessageActivity(){
        super(R.layout.activity_message);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"消息");
        item_push_setting.setTVStyle(R.mipmap.sys_msg,"系统通知", R.color.color_333, false);
        item_clear_cache.setTVStyle(R.mipmap.icon_comment,"评论", R.color.color_333, false);
        item_to_comment.setTVStyle(R.mipmap.private_msg,"私信",R.color.color_333, false);
//        item_welcome_page.setTVStyle(R.mipmap.icon_mention,"提到我的",R.color.color_333, false);
        item_feedback.setTVStyle(R.mipmap.icon_follow,"关注我的", R.color.color_333, false);
        item_about_us.setTVStyle(R.mipmap.icon_good,"收到的赞", R.color.color_333, false);
    }

    @OnClick({R.id.item_push_setting,R.id.item_clear_cache,R.id.item_to_comment,R.id.item_about_us,R.id.item_feedback})
    void onClick(View v){
        Intent intent=null;
        switch (v.getId()){
            case R.id.item_push_setting:
                Util.makeToast("系统通知");
                break;
            case R.id.item_clear_cache: //评论列表
                startActivity(new Intent(activity, CommentListActivity.class));
                break;
            case R.id.item_to_comment:
                Util.makeToast("私信");
                break;
            case R.id.item_about_us: //收到的赞
                Util.makeToast("收到的赞");
                break;
            case R.id.item_feedback: //关注我的
                intent= new Intent(activity, FocusFansActivity.class);
                intent.putExtra(FocusFansActivity.class.getSimpleName(),FocusFansActivity.FANS_TYPE);
                startActivity(intent);
                break;
        }
    }
}
