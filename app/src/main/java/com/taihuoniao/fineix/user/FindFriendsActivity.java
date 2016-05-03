package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomSubItemLayout;
import com.taihuoniao.fineix.zxing.activity.CaptureActivity;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author lilin
 * created at 2016/4/26 16:06
 */
public class FindFriendsActivity extends BaseActivity implements PlatformActionListener {
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
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 3:
                    Util.makeToast("对不起，分享出错");
                    break;
                case 2:
                    Util.makeToast("您取消了分享");
                    break;
                case 1:
                    Util.makeToast("分享成功");
                    break;
            }
        }
    };
    @OnClick({R.id.head_view_shop,R.id.item_wx,R.id.item_sina,R.id.item_contacts})
    void onClick(View v){
        Platform.ShareParams params=null;
        switch (v.getId()){
            case R.id.head_view_shop:
                startActivity(new Intent(activity, CaptureActivity.class));
                break;
            case R.id.item_wx:
                //wechat
                params = new Platform.ShareParams();
                params.setShareType(Platform.SHARE_WEBPAGE);
                params.setUrl("Constants.SHARE_URI + detail.goods_id");
                params.setTitle("detail.goods_name");
                params.setText("detail.goods_name");
                params.setImageUrl("Constants.PIC_URI+detail.goods_img");
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(this);
                wechat.share(params);
                break;
            case R.id.item_sina:
                //sina
                params = new Platform.ShareParams();
                params.setTitle("detail.goods_name");
                params.setText("detail.goods_name");
                params.setImageUrl("Constants.PIC_URI+detail.goods_img");
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(this); // 设置分享事件回调
                weibo.share(params);
                break;
            case R.id.item_contacts:
                String content="我是分享内容....";
                Uri sms = Uri.parse("smsto:");
                Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, sms);
                sendIntent.putExtra( "sms_body",content);
                sendIntent.setType("vnd.android-dir/mms-sms" );
                startActivity(sendIntent);
                break;
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(2);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        handler.sendEmptyMessage(3);
    }
}
