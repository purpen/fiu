package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomSubItemLayout;
import com.taihuoniao.fineix.zxing.activity.CaptureActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/4/26 16:06
 */
public class FindFriendsActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.item_wx)
    CustomSubItemLayout item_wx;
    @Bind(R.id.item_sina)
    CustomSubItemLayout item_sina;
    @Bind(R.id.item_contacts)
    CustomSubItemLayout item_contacts;
    public FindFriendsActivity(){
        super(R.layout.activity_find_freinds);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"发现好友");
        custom_head.setHeadShopShow(true);
        custom_head.getShopImg().setImageResource(R.mipmap.scan);
        item_wx.setImg(R.mipmap.wechat);
        item_wx.setTitle("邀请微信好友");
        item_wx.setSubTitle("分享给好友");
        item_sina.setImg(R.mipmap.sina);
        item_sina.setTitle("连接微博");
        item_sina.setSubTitle("分享给微博好友");
        item_contacts.setImg(R.mipmap.wechat);
        item_contacts.setTitle("连接通讯录");
        item_contacts.setSubTitle("关注你认识的好友");
    }

    @OnClick({R.id.head_view_shop,R.id.item_wx,R.id.item_sina,R.id.item_contacts})
    void onClick(View v){
        switch (v.getId()){
            case R.id.head_view_shop:
                startActivity(new Intent(activity, CaptureActivity.class));
                break;
            case R.id.item_wx:
                Util.makeToast("微信分享");
                break;
            case R.id.item_sina:
                Util.makeToast("微博分享");
                break;
            case R.id.item_contacts:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(intent.EXTRA_TEXT,"我是分享内容....");
                intent.setType("*/*");
                startActivity(Intent.createChooser(intent, "分享到"));
                break;
        }
    }
}
