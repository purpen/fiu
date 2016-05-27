package com.taihuoniao.fineix.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author lilin
 * created at 2016/5/15 23:46
 */
public class CustomShareView extends RelativeLayout implements PlatformActionListener {
    private GridView gv_share;
    private View tv_cancel;
//    private GoodsDetail detail;
    private Context context;
    public CustomShareView(Context context){
        super(context);
        this.context=context;
//        this.detail=detail;
        initView(context);
    }
    public CustomShareView(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context=context;
//        this.detail=detail;
        initView(context);
    }
    public CustomShareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
//        this.detail=detail;
        initView(context);
    }

    private void initView(Context context){
        View view = Util.inflateView(context, R.layout.share_layout, this);
        gv_share = (GridView) view.findViewById(R.id.gv_share);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        int[] image={R.mipmap.wechat,R.mipmap.wechatmoment,R.mipmap.sina,R.mipmap.qqzone};
        String[] name={"微信好友","微信朋友圈","新浪微博","QQ空间",};
        List<HashMap<String, Object>> shareList=new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<image.length;i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", image[i]);
            map.put("text", name[i]);
            shareList.add(map);
        }
        SimpleAdapter adapter=new SimpleAdapter(context, shareList, R.layout.share_item_layout,new String[] {"image","text"}, new int[] {R.id.iv_plat_logo,R.id.tv_plat_name});
        gv_share.setAdapter(adapter);
        gv_share.setOnItemClickListener(itemClicklistener);
        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.dismiss();
            }
        });
    }

    private AdapterView.OnItemClickListener itemClicklistener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Platform.ShareParams params=null;
            switch (position){
//                case 0:
//                    //qq
//                    params=new Platform.ShareParams();
////                    params.setTitle(getResources().getString(R.string.share_title_url));
//                    params.setText(getResources().getString(R.string.share_title_url));
////                    params.setTitleUrl("http://www.taihuoniao.com/");
////                    params.setImageUrl(LoginInfo.getHeadPicUrl());
//                    Platform qq = ShareSDK.getPlatform(QQ.NAME);
//                    qq.setPlatformActionListener(CustomShareView.this);
//                    qq.share(params);
//                    break;
                case 3:
                    //qqzong
                    params= new Platform.ShareParams();
                    params.setText(getResources().getString(R.string.share_title_url));
//                    params.setTitle(getResources().getString(R.string.share_title_url));
//                    params.setText("有Fiu的生活，才够意思，快点扫码加我吧！查看个人主页>>http://www.taihuoniao.com/");
//                    params.setTitleUrl("http://www.taihuoniao.com/"); // 标题的超链接
//                    params.setImageUrl(LoginInfo.getHeadPicUrl());
//                    params.setSite(context.getString(R.string.app_name));
//                    params.setSiteUrl("http://www.taihuoniao.com/");
                    Platform qzone = ShareSDK.getPlatform (QZone.NAME);
                    qzone.setPlatformActionListener (CustomShareView.this); // 设置分享事件回调
                    qzone.share(params);
                    break;
                case 2:
                    //sina
                    params = new Platform.ShareParams();
//                    params.setTitle(getResources().getString(R.string.share_title_url));
                    params.setText(getResources().getString(R.string.share_title_url));
//                    params.setImageUrl(LoginInfo.getHeadPicUrl());
                    Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    weibo.setPlatformActionListener(CustomShareView.this); // 设置分享事件回调
                    weibo.share(params);
                    break;
                case 0:
                    //wechat
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_TEXT);
//                params.setUrl("http://m.taihuoniao.com/guide/fiu");
//                params.setTitle(getResources().getString(R.string.share_title_url));
                    params.setText(getResources().getString(R.string.share_title_url));
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(CustomShareView.this);
                    wechat.share(params);
                    break;
                case 1:
                    //wechatmoment
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_TEXT);
//                    params.setUrl("http://www.taihuoniao.com/");
//                    params.setTitle(getResources().getString(R.string.share_title_url));
                    params.setText(getResources().getString(R.string.share_title_url));
//                    params.setImageUrl(LoginInfo.getHeadPicUrl());
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(CustomShareView.this);
                    wechatMoments.share(params);
                    break;
//                case 5:
//                    //wefa
//                    params = new Platform.ShareParams();
//                    params.setShareType(Platform.SHARE_WEBPAGE);
//                    params.setUrl("");
//                    params.setTitle("detail.goods_name");
//                    params.setText("detail.goods_name");
//                    params.setImageUrl("Constants.PIC_URI+detail.goods_img");
//                    Platform wechatFavorite = ShareSDK.getPlatform(WechatFavorite.NAME);
//                    wechatFavorite.setPlatformActionListener(CustomShareView.this);
//                    wechatFavorite.share(params);
//                    break;
            }
        }
    };

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 3:
                    Util.makeToast(context,"对不起，分享出错");
                    break;
                case 2:
                    Util.makeToast(context,"您取消了分享");
                    break;
                case 1:
                    Util.makeToast(context,"分享成功");
                    break;
            }
        }
    };

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
